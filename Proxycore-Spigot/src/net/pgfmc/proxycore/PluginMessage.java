package net.pgfmc.proxycore;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.io.CountingInputStream;

import net.pgfmc.proxycore.util.Logger;

public abstract class PluginMessage implements PluginMessageListener {
	
	public static final String CHANNEL_PGF = "pgf:main";
	public static final String CHANNEL_BUNGEECORD = "BungeeCord"; // bungeecord:main
	
	private final String channel;
	private final String subchannel;
	
	public PluginMessage(final PluginMessageType messenger)
	{
		channel = messenger.channel;
		subchannel = messenger.subchannel;
		
		Main.plugin.getServer().getMessenger().registerIncomingPluginChannel(Main.plugin, channel, this);
		
	}
	
	public PluginMessage(final String channel, final String subchannel)
	{
		this.channel = channel;
		this.subchannel = subchannel;
		
		Main.plugin.getServer().getMessenger().registerIncomingPluginChannel(Main.plugin, channel, this);
		
	}
	
	public enum PluginMessageType {
		GET_SERVERS(PluginMessage.CHANNEL_BUNGEECORD, "GetServers"),
		GET_SERVER(PluginMessage.CHANNEL_BUNGEECORD, "GetServer"),
		CONNECT(PluginMessage.CHANNEL_PGF, "Connect"),
		PING_SERVER(PluginMessage.CHANNEL_PGF, "PingServer");
		
		public final String channel;
		public final String subchannel;
		
		private PluginMessageType(final String channel, final String subchannel)
		{
			this.channel = channel;
			this.subchannel = subchannel;
			
			if (!Main.plugin.getServer().getMessenger().isOutgoingChannelRegistered(Main.plugin, channel))
			{
				Main.plugin.getServer().getMessenger().registerOutgoingPluginChannel(Main.plugin, channel);
			}
			
		}
		
		@Override
		public String toString()
		{
			return subchannel;
		}
		
		public static final PluginMessageType from(final String channel, final String subchannel)
		{
			for (final PluginMessageType messenger : PluginMessageType.values())
			{
				if (messenger.channel.equals(channel) && messenger.subchannel.equals(subchannel)) return messenger;
			}
			
			return null;
		}
		
		public final CompletableFuture<List<String>> send(final Player player, final List<String> args)
		{
			Logger.debug("------------------------------");
			Logger.log("Sending plugin message and creating a Future.");
			Logger.log("Channel: " + channel);
			Logger.log("Subchannel: " + subchannel);
			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			
			out.writeUTF(subchannel);
			
			for (final String arg : args)
			{
				Logger.log("arg: " + arg);
				out.writeUTF(arg);
			}
			
			player.sendPluginMessage(Main.plugin, channel, out.toByteArray());
			
			final PluginMessageType pluginMessageType = this;
			
			final CompletableFuture<List<String>> future = new CompletableFuture<List<String>>();
			
			final Thread thread = new Thread(new Runnable() {
				
				class FuturePluginMessage extends PluginMessage {

					public FuturePluginMessage() {
						super(pluginMessageType);
					}

					@Override
					public void onPluginMessageReceived(final Player player, final List<String> args) {
						Logger.debug("Completing Future: " + pluginMessageType.name());
						future.complete(args);
					}
					
				}
				
				@Override
				public void run() {
					new FuturePluginMessage();
					
				}
				
			});
			
			thread.start();
			
			return future.whenComplete((arguments, exception) -> {
				thread.interrupt();
			});
			
		}
		
		public final CompletableFuture<List<String>> send(final Player player, final String arg0, final String arg1, final String arg2, final String arg3)
		{
			return send(player, Arrays.asList(arg0, arg1, arg2, arg3));
		}
		
		public final CompletableFuture<List<String>> send(final Player player, final String arg0, final String arg1, final String arg2)
		{
			return send(player, Arrays.asList(arg0, arg1, arg2));
		}
		
		public final CompletableFuture<List<String>> send(final Player player, final String arg0, final String arg1)
		{
			return send(player, Arrays.asList(arg0, arg1));
		}
		
		public final CompletableFuture<List<String>> send(final Player player, final String arg0)
		{
			return send(player, Arrays.asList(arg0));
		}
		
		public final CompletableFuture<List<String>> send(final Player player)
		{
			return send(player, new ArrayList<String>());
		}

	}
	
	public abstract void onPluginMessageReceived(final Player player, final List<String> args);
	
	@Override
	public final void onPluginMessageReceived(String channel, Player player, byte[] message)
	{
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
		
		if (subchannel == null || !subchannel.equals(this.subchannel)) return;
		
		Logger.debug("------------------------------");
		Logger.debug("Plugin Message received.");
		Logger.debug("Channel: " + channel);
		Logger.debug("Player: " + player);
		Logger.debug("Subchannel: " + subchannel);
		Logger.debug("Attempting to get response:");
		
		final List<String> args = new ArrayList<String>();
		args.add(subchannel);
		
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
		
		onPluginMessageReceived(player, args);
		
		Main.plugin.getServer().getMessenger().unregisterIncomingPluginChannel(Main.plugin, channel, this);
		
	}

}


