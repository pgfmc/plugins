package net.pgfmc.proxycore.util.pluginmessage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.io.CountingInputStream;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;

/**
 * Listen for plugin messages of a single type
 */
public abstract class PluginMessage {
	
	private final PluginMessageType type;
	
	/**
	 * 
	 * @param type The plugin message type to listen for
	 */
	public PluginMessage(final PluginMessageType type)
	{
		this.type = type;
		
		// Register this class to be a listener
		Main.plugin.proxy.getEventManager().register(Main.plugin, this);
		
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
	 * Is called when the PluginMessageType is received
	 * 
	 * @param connection The backend server this plugin message came from
	 * @param player The player that sent this plugin message
	 * @param args The arguments
	 */
	public abstract void onPluginMessageReceived(final ServerConnection connection, final Player player, final List<String> args);
	
	@Subscribe
	public final void onPluginMessageFromPlugin(PluginMessageEvent event)
	{
		if (!(event.getSource() instanceof ServerConnection)) return;
    	if (event.getIdentifier() != Main.IDENTIFIER) return;
		
		final InputStream inputStream = new ByteArrayInputStream(event.getData());
		final CountingInputStream counter = new CountingInputStream(inputStream);
		final DataInputStream dataIn = new DataInputStream(counter);
		
		String subchannel = null;
		try {
			subchannel = dataIn.readUTF();
		} catch (IOException e) {
			Logger.error("Subchannel not found in PM:");
			e.printStackTrace();
		}
		
		if (subchannel == null || !Objects.equals(type.subchannel, subchannel)) return;
		
		Logger.debug("------------------------------");
		Logger.debug("Plugin Message received.");
		Logger.debug("Channel: " + Main.IDENTIFIER);
		Logger.debug("Sender: " + event.getSource());
		Logger.debug("Subchannel: " + subchannel);
		Logger.debug("Attempting to get args:");
		
		// Convert the byte array to a string list (for convenience)
		final List<String> args = new ArrayList<String>();
		args.add(subchannel); // add subchannel
		
		// add any arguments
		try {
			while (dataIn.available() != 0)
			{
				args.add(dataIn.readUTF());
			}
		} catch (IOException e) {
			Logger.error("Error while trying to get plugin message args: ");
			
			e.printStackTrace();
		}
		
		Logger.debug(args.toString());
		
		final ServerConnection connection = (ServerConnection) event.getSource();
		
		// Everything matches. Call the method
		onPluginMessageReceived(connection, connection.getPlayer(), args);
		
	}

}
