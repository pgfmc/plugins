package net.pgfmc.proxycore.util.proxy;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.common.io.CountingInputStream;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;

/**
 * Listen for plugin messages of a single type
 */
public abstract class PluginMessage {
	
	private final PluginMessageType type;
	private final String channel;
	private final String subchannel;
	
	/**
	 * 
	 * @param type The plugin message type to listen for
	 */
	public PluginMessage(final PluginMessageType type)
	{
		this.type = type;
		this.channel = null;
		this.subchannel = null;
		
		// Register this class to be a listener
		Main.plugin.proxy.getEventManager().register(Main.plugin, this);
		
	}
	
	/**
	 * 
	 * @param channel
	 * @param subchannel
	 */
	public PluginMessage(final String channel, final String subchannel)
	{
		this.type = null;
		this.channel = channel;
		this.subchannel = subchannel;
		
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
	public abstract void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message);
	
	@Subscribe
	public final void onPluginMessageFromPlugin(PluginMessageEvent event)
	{
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
		
		if (subchannel == null) return;
		
		if (type == null)
		{
			if (!Objects.equals(this.channel, channel) || !Objects.equals(this.subchannel, subchannel)) return;
		} else
		{
			if (!Objects.equals(type.subchannel, subchannel)) return;
		}
		
		Logger.debug("------------------------------");
		Logger.debug("Plugin Message received.");
		Logger.debug("Channel: " + Main.IDENTIFIER);
		Logger.debug("Sender: " + event.getSource());
		Logger.debug("Subchannel: " + subchannel);
		
		final ChannelMessageSource source = event.getSource();
		final byte[] message = event.getData();
		
		// Everything matches. Call the method
		onPluginMessageReceived(source, ByteStreams.newDataInput(message), message);
		
	}

}
