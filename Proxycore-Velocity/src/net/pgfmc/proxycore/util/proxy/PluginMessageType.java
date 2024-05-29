package net.pgfmc.proxycore.util.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.messages.ChannelMessageSink;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;

/**
 * Used for sending a plugin message
 */
public enum PluginMessageType {
	CONNECT("Connect"),
	PING_SERVER("PingServer"),
	DISCORD_MESSAGE("DiscordMessage"),
	PLAYER_DATA("PlayerData");
	
	final String subchannel;
	
	private PluginMessageType(final String subchannel)
	{
		this.subchannel = subchannel;
		
	}

	@Override
	public String toString()
	{
		return subchannel;
	}
	
	public final String getSubchannel()
	{
		return subchannel;
	}
	
	public static final PluginMessageType from(final String subchannel)
	{
		for (final PluginMessageType type : PluginMessageType.values())
		{
			if (type.subchannel.equals(subchannel)) return type;
		}
		
		return null;
	}
	
	/**
	 * Send a plugin message with this type
	 * 
	 * @param sender A Player or a ServerConnection
	 * @param args The arguments. Do not include the subchannel
	 */
	public final void send(final ChannelMessageSink sender, final List<String> args)
	{
		Logger.debug("------------------------------");
		Logger.debug("Sending plugin message.");
		Logger.debug("Channel: " + Main.IDENTIFIER);
		Logger.debug("Subchannel: " + subchannel);
		
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		// add subchannel
		out.writeUTF(subchannel);
		
		// include any arguments
		for (final String arg : args)
		{
			Logger.debug("arg: " + arg);
			out.writeUTF(arg);
		}
		
		sender.sendPluginMessage(Main.IDENTIFIER, out.toByteArray());
		
	}
	
	public final void send(final ChannelMessageSink sender, final String... arguments)
	{
		send(sender, Arrays.asList(arguments));
	}
	
	public final void send(final ChannelMessageSink sender)
	{
		send(sender, new ArrayList<String>());
	}
	
}
