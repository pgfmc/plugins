package net.pgfmc.proxycore.util.proxy;

import java.util.List;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.messages.ChannelMessageSink;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.roles.PGFRole;
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
	private final void sendPluginMessage(final ChannelMessageSink sender, final List<Object> arguments)
	{
		Logger.debug("------------------------------");
		Logger.debug("Sending plugin message.");
		Logger.debug("Channel: " + Main.IDENTIFIER);
		Logger.debug("Subchannel: " + subchannel);
		
		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		// add subchannel
		out.writeUTF(subchannel);
		
		// include any arguments
		for (final Object arg : arguments)
		{
			
			if (arg instanceof String) {
				out.writeUTF((String) arg);
				
			} else if (arg instanceof Integer) {
				out.writeInt((Integer) arg);
				
			} else if (arg instanceof Boolean) {
				out.writeBoolean((Boolean) arg);
				
			} else if (arg instanceof Double) {
				out.writeDouble((Double) arg);
				
			} else if (arg instanceof Float) {
				out.writeFloat((Float) arg);
				
			} else if (arg instanceof Long) {
				out.writeLong((Long) arg);
				
			} else if (arg instanceof Short) {
				out.writeShort((Short) arg);
				
			} else if (arg instanceof PGFRole) {
				out.writeUTF(((PGFRole) arg).name());
				
			} else
			{
				Logger.warn("Cannot send unsupported data type: " + arg.getClass().toString());
				return;
			}
			
		}
		
		sender.sendPluginMessage(Main.IDENTIFIER, out.toByteArray());
		
	}
	
	public final void send(final ChannelMessageSink sender, final Object... arguments)
	{
		sendPluginMessage(sender, List.of(arguments));
	}
	
	public final void send(final ChannelMessageSink sender)
	{
		sendPluginMessage(sender, List.of());
	}
	
}
