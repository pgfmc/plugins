package net.pgfmc.core.util.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.Logger;

/**
 * A list of plugin message types from PGF and BungeeCord
 */
public enum PluginMessageType {
	// bungeecord:main
	//IP(PluginMessage.CHANNEL_BUNGEECORD, "IP", null),
	//PLAYER_COUNT(PluginMessage.CHANNEL_BUNGEECORD, "PlayerCount", PluginMessageMatchType.ARGUMENT),
	PLAYER_LIST(PluginMessage.CHANNEL_BUNGEECORD, "PlayerList", PluginMessageMatchType.ARGUMENT),
	GET_SERVERS(PluginMessage.CHANNEL_BUNGEECORD, "GetServers", PluginMessageMatchType.NONE),
	MESSAGE(PluginMessage.CHANNEL_BUNGEECORD, "Message", PluginMessageMatchType.NONE),
	//MESSAGE_RAW(PluginMessage.CHANNEL_BUNGEECORD, "MessageRaw", PluginMessageMatchType.NONE),
	GET_SERVER(PluginMessage.CHANNEL_BUNGEECORD, "GetServer", PluginMessageMatchType.SENDER),
	UUID(PluginMessage.CHANNEL_BUNGEECORD, "UUID", PluginMessageMatchType.SENDER),
	UUID_OTHER(PluginMessage.CHANNEL_BUNGEECORD, "UUIDOther", PluginMessageMatchType.ARGUMENT),
	//SERVER_IP(PluginMessage.CHANNEL_BUNGEECORD, "ServerIp", PluginMessageMatchType.ARGUMENT), // no response from proxy
	KICK_PLAYER(PluginMessage.CHANNEL_BUNGEECORD, "KickPlayer", PluginMessageMatchType.NONE),
	//FORWARD(PluginMessage.CHANNEL_BUNGEECORD, "Forward", null),
	//FORWARD_TO_PLAYER(PluginMessage.CHANNEL_BUNGEECORD, "ForwardToPlayer", null),
	// pgf:main
	CONNECT(PluginMessage.CHANNEL_PGF, "Connect", PluginMessageMatchType.SENDER),
	PING_SERVER(PluginMessage.CHANNEL_PGF, "PingServer", PluginMessageMatchType.ARGUMENT),
	PLAYER_ROLE(PluginMessage.CHANNEL_PGF, "GetPlayerRole", PluginMessageMatchType.ARGUMENT),
	DISCORD_MESSAGE(PluginMessage.CHANNEL_PGF, "DiscordMessage", PluginMessageMatchType.NONE);
	
	/**
	 * Determines how to check if the plugin message response matches the original plugin message
	 */
	private enum PluginMessageMatchType {
		SENDER, // Match by checking the players
		ARGUMENT, // Match by checking the arguments (usually playername or servername)
		NONE; // Does not matter or no response
	}
	
	/**
	 * 
	 * 
	 */
	final String channel;
	final String subchannel;
	private final PluginMessageMatchType matchType;
	
	/**
	 * 
	 * @param channel The channel identifier for the plugin message type
	 * @param subchannel The plugin message type
	 */
	private PluginMessageType(final String channel, final String subchannel, final PluginMessageMatchType matchType)
	{
		this.channel = channel;
		this.subchannel = subchannel;
		this.matchType = matchType;
		
		// Register the outoging channel if it isn't already
		if (!CoreMain.plugin.getServer().getMessenger().isOutgoingChannelRegistered(CoreMain.plugin, channel))
		{
			CoreMain.plugin.getServer().getMessenger().registerOutgoingPluginChannel(CoreMain.plugin, channel);
		}
		
	}
	
	/**
	 * Returns the subchannel
	 * 
	 * This method is equal to getSubchannel()
	 */
	@Override
	public String toString()
	{
		return subchannel;
	}
	
	/**
	 * @return The channel identifier
	 */
	public final String getChannel()
	{
		return channel;
	}
	
	/**
	 * @return The subchannel
	 */
	public final String getSubchannel()
	{
		return subchannel;
	}
	
	/**
	 * Attempts to find a PluginMessageType with a subchannel that
	 * matches this subchannel
	 * @param subchannel The subchannel to look for
	 * @return The PluginMessageType or null
	 */
	public static final PluginMessageType from(final String subchannel)
	{
		for (final PluginMessageType type : PluginMessageType.values())
		{
			if (type.subchannel.equals(subchannel)) return type; // match
		}
		
		return null; // no match
	}
	
	/**
	 * Send a plugin message.
	 * 
	 * CompletableFuture is completed when the listener receives a response
	 * that matches the sent plugin message.
	 * 
	 * @param player The proxied player to send the plugin message through
	 * @param args An ordered list of arguments required by the plugin message type (do not include the type in the arguments)
	 * @return A CompletableFuture that holds an ordered List<String> of the plugin message response (if any)
	 */
	public final CompletableFuture<List<String>> send(final PluginMessageRecipient sender, final List<String> args)
	{
		Logger.debug("------------------------------");
		Logger.log("Sending plugin message and creating a Future.");
		Logger.log("Channel: " + channel);
		Logger.log("Subchannel: " + subchannel);
		
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		// add subchannel
		out.writeUTF(subchannel);
		
		// add any arguments
		for (final String arg : args)
		{
			Logger.log("arg: " + arg);
			out.writeUTF(arg);
		}
		
		// send plugin message
		sender.sendPluginMessage(CoreMain.plugin, channel, out.toByteArray());
		
		// return a completable future
		return createFuture(this, sender, args);
	}
	
	/**
	 * CompletableFuture is completed when the listener receives a response
	 * that matches the sent plugin message.
	 * 
	 * @param originalType
	 * @param originalSender
	 * @param originalArgs
	 * @return A CompletableFuture that holds an ordered List<String> of the plugin message response (if any)
	 */
	private static final CompletableFuture<List<String>> createFuture(final PluginMessageType originalType, final PluginMessageRecipient originalSender, final List<String> originalArgs)
	{
		// the future
		final CompletableFuture<List<String>> future = new CompletableFuture<List<String>>()
				.orTimeout(5L, TimeUnit.MINUTES); // default timeout is 5 minutes.
		
		// new thread used to asynchronously listen for plugin messages
		final Thread thread = new Thread(new Runnable() {
			// class used to extend PluginMessage for the listener
			class FuturePluginMessage extends PluginMessage {
				
				final PluginMessageRecipient originalSender;
				final List<String> originalArgs;

				public FuturePluginMessage(final PluginMessageRecipient originalSender, final List<String> originalArgs) {
					super(originalType); // The type to listen for
					
					this.originalSender = originalSender;
					this.originalArgs = originalArgs;
				}

				@Override
				public void onPluginMessageTypeReceived(final Player sender, final List<String> args) {
					
					switch(originalType.matchType) {
					case SENDER: // Check original sender against this sender
						if (!Objects.equals(originalSender, sender)) return;
						
						break;
					case ARGUMENT: // Check original argument with this argument
						if (args.size() < 2 || originalArgs.size() < 1)
						{
							// Error here,,, This shouldn't ever happen.
							Logger.error("Error: PluginMessageType.onPluginMessageReceived has incorrect number of arguments.");
							Logger.error("Type: " + originalType.name());
							Logger.error("Original Sender: " + originalSender.toString());
							Logger.error("Original Arguments: " + originalArgs.toString());
							Logger.error("Sender: " + sender.getName());
							Logger.error("Arguments: " + args.toString());
							
							return;
						}
						
						if (!Objects.equals(originalArgs.get(0), args.get(1))) return;
						
						break;
					case NONE: // Only channel and subchannel needs to match
					default:
						break;
					}
					
					Logger.debug("Completing Future: " + originalType.name());
					
					CoreMain.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(CoreMain.plugin, getType().channel, this);
					
					// complete future with the response arguments
					future.complete(args);
				}
				
			}
			
			@Override
			public void run() {
				new FuturePluginMessage(originalSender, originalArgs);
				
			}
			
		});
		
		// start the thread
		thread.start();
		
		return future
				.whenComplete((arguments, exception) -> {
					Logger.debug("A thread is done with type: " + originalType.name());
					thread.interrupt(); // End the thread to prevent memory leak
				});
	}
	
	public final CompletableFuture<List<String>> send(final PluginMessageRecipient sender, final String... arguments)
	{
		return send(sender, Arrays.asList(arguments));
	}
	
	public final CompletableFuture<List<String>> send(final PluginMessageRecipient sender)
	{
		return send(sender, new ArrayList<String>());
	}

}
