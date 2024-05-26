package net.pgfmc.proxycore.util.pluginmessage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.CountingInputStream;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;

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
		Main.plugin.getServer().getMessenger().registerIncomingPluginChannel(Main.plugin, channel, this);
		
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
		Main.plugin.getServer().getMessenger().registerIncomingPluginChannel(Main.plugin, channel, this);
		
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
	public abstract void onPluginMessageReceived(final Player sender, final List<String> response);
	
	@Override
	public final void onPluginMessageReceived(String channel, Player sender, byte[] message)
	{
		// channel needs to match
		if (!channel.equals(this.channel)) return;
		
		final InputStream inputStream = new ByteArrayInputStream(message);
		final CountingInputStream counter = new CountingInputStream(inputStream);
		final DataInputStream dataIn = new DataInputStream(counter);
		
		String subchannel = null;
		try {
			subchannel = dataIn.readUTF();
		} catch (IOException e) {
			Logger.error("Subchannel not found in PM:");
			e.printStackTrace();
		}
		
		// subchannel needs to match
		if (!Objects.equals(subchannel, this.subchannel)) return;
		
		Logger.debug("------------------------------");
		Logger.debug("Plugin Message received.");
		Logger.debug("Channel: " + channel);
		Logger.debug("Sender: " + sender);
		Logger.debug("Subchannel: " + subchannel);
		Logger.debug("Attempting to get response:");
		
		// convert response from byte array to string list (for convenience)
		final List<String> response = new ArrayList<String>();
		response.add(subchannel);
		
		try {
			while (dataIn.available() != 0)
			{
				response.add(dataIn.readUTF());
			}
		} catch (IOException e) {
			Logger.error("Error while trying to get plugin message response: ");
			
			e.printStackTrace();
		}
		
		Logger.debug(response.toString());
		
		// Call the listeners that have this type
		onPluginMessageReceived(sender, response); // additional checks happen for PluginMessageType futures
		
		Main.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(Main.plugin, channel, this);
		
	}

}
