package net.pgfmc.proxycore.util.pluginmessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;

/**
 * Used for sending a plugin message
 */
public enum PluginMessageType {
	CONNECT("Connect"),
	PING_SERVER("PingServer");
	
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
	public final void send(final ChannelMessageSource sender, final List<String> args)
	{
		Logger.debug("------------------------------");
		Logger.log("Sending plugin message and creating a Future.");
		Logger.log("Channel: " + Main.IDENTIFIER);
		Logger.log("Subchannel: " + subchannel);
		
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		// add subchannel
		out.writeUTF(subchannel);
		
		// include any arguments
		for (final String arg : args)
		{
			Logger.log("arg: " + arg);
			out.writeUTF(arg);
		}
		
		// Make the bridge with a Player
		if (sender instanceof Player)
		{
			// Only one plugin message will be sent
			((Player) sender).sendPluginMessage(Main.IDENTIFIER, out.toByteArray());
		}
		
		// Make the bridge with a ServerConnection
		if (sender instanceof ServerConnection)
		{
			// This plugin message will be sent once for each online player.
			// If no players are online, the backend server will not receive this plugin message.
			((ServerConnection) sender).sendPluginMessage(Main.IDENTIFIER, out.toByteArray());
		}
		
	}
	
	public final void send(final ChannelMessageSource sender, final String arg0, final String arg1, final String arg2, final String arg3)
	{
		send(sender, Arrays.asList(arg0, arg1, arg2, arg3));
	}
	
	public final void send(final ChannelMessageSource sender, final String arg0, final String arg1, final String arg2)
	{
		send(sender, Arrays.asList(arg0, arg1, arg2));
	}
	
	public final void send(final ChannelMessageSource sender, final String arg0, final String arg1)
	{
		send(sender, Arrays.asList(arg0, arg1));
	}
	
	public final void send(final ChannelMessageSource sender, final String arg0)
	{
		send(sender, Arrays.asList(arg0));
	}
	
	public final void send(final ChannelMessageSource sender)
	{
		send(sender, new ArrayList<String>());
	}

}
