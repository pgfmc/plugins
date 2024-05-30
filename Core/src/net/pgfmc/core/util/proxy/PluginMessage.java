package net.pgfmc.core.util.proxy;

import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.util.Logger;

/**
 * CONTEXT:
 * 
 * Minecraft plugin messages are used to communicate from the backend server
 * to the proxy and vice versa. Plugin messages being sent to a backend server
 * require a player to act as a bridge between the proxy and the backend server.
 * Without this bridge, backend servers cannot receive plugin messages. The proxy
 * can receive plugin messages without the bridge however.
 * 
 * Plugin message documentation can be found here:
 * - https://docs.papermc.io/paper/dev/plugin-messaging
 * 
 * --------------------------------------------------------------------
 * 
 * Extend the abstract PluginMessage class to register a new PluginMessageListener (useful for
 * receiving events for the specified PluginMessageType without needing to send a plugin message).
 * One can send plugin messages within the class if needed.
 * 
 * Use the PluginMessageType enum send method to send a plugin message. The send method returns a
 * CompletableFuture that holds an ordered List<String> of the plugin message response.
 * The returned completable future can be used to handle the expected plugin message response.
 * 
 * --------------------------------------------------------------------
 * 
 * Unable to decide between using PluginMessage or PluginMessageType?
 * 
 * Use PluginMessage if you need to listen for all plugin messages of a single type.
 * - Caters more to receiving plugin messages
 * 
 * Use PluginMessageType if you just need to send a single plugin message and handle the response.
 * - Caters more to sending plugin messages
 */
public abstract class PluginMessage implements PluginMessageListener {
	
	/**
	 * Channel identifiers for PGF and BungeeCord
	 * 
	 * See the documentation for BungeeCord plugin message types:
	 * - https://docs.papermc.io/paper/dev/plugin-messaging#plugin-message-types
	 */
	public static final String CHANNEL_PGF = "pgf:main";
	public static final String CHANNEL_BUNGEECORD = "BungeeCord"; // bungeecord:main
	
	private final PluginMessageType type;
	private final String channel;
	private final String subchannel;
	
	/**
	 * Listen for the plugin message type
	 * 
	 * @param pluginMessageType The plugin message type
	 */
	public PluginMessage(final PluginMessageType type)
	{
		this.type = type;
		channel = type.channel;
		subchannel = type.subchannel;
		
		// Register PluginMessageListener for this class
		CoreMain.plugin.getServer().getMessenger().registerIncomingPluginChannel(CoreMain.plugin, channel, this);
		
	}
	
	/**
	 * Listen for a plugin message type that isn't defined in PluginMessageType
	 * 
	 * @param channel The channel identifier for the plugin message type
	 * @param subchannel The plugin message type
	 */
	public PluginMessage(final String channel, final String subchannel)
	{
		this.type = PluginMessageType.from(subchannel);
		this.channel = channel;
		this.subchannel = subchannel;
		
		// Register PluginMessageListener for this class
		CoreMain.plugin.getServer().getMessenger().registerIncomingPluginChannel(CoreMain.plugin, channel, this);
		
	}
	
	/**
	 * 
	 * @return the plugin message type
	 */
	public final PluginMessageType getType()
	{
		return type;
	}
	
	/**
	 * 
	 * @return the channel identifier
	 */
	public final String getChannel()
	{
		return channel;
	}
	
	/**
	 * 
	 * @return the subchannel
	 */
	public final String getSubchannel()
	{
		return subchannel;
	}
	
	/**
	 * Is called when the PluginMessageType is received
	 * 
	 * @param sender The player that bridged this connection
	 * @param response The response
	 */
	public abstract void onPluginMessageTypeReceived(final Player sender, ByteArrayDataInput in, final byte[] message);
	
	@Override
	public final void onPluginMessageReceived(String channel, Player sender, byte[] message)
	{
		Logger.debug("------------------------------");
		Logger.debug("Plugin Message received.");
		Logger.debug("Channel: " + channel);
		Logger.debug("Sender: " + sender);
		
		// channel needs to match
		if (!channel.equals(this.channel)) return;
		
		final ByteArrayDataInput in = ByteStreams.newDataInput(message);
		final String subchannel = in.readUTF();
		
		Logger.debug("Subchannel: " + subchannel);
		
		// subchannel needs to match
		if (!Objects.equals(subchannel, this.subchannel)) return;
		
		// Call the listeners that have this type
		onPluginMessageTypeReceived(sender, ByteStreams.newDataInput(message), message); // additional checks happen for PluginMessageType futures
		
	}

}
