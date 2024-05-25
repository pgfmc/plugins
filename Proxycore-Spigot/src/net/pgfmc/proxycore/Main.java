package net.pgfmc.proxycore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.proxycore.serverselector.ConnectCommand;
import net.pgfmc.proxycore.util.Logger;

public class Main extends JavaPlugin implements Listener, PluginMessageListener, Logger {
	
	public static String thisServerName;
	public static Main plugin;
	/**
	 * Registered servers' names and if they are currently online
	 * 
	 * Online status is updated by Main.loopForPluginMessages()
	 */
	public final static Map<String, Boolean> registeredServers = new HashMap<>();
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		/**
		 * Register Listeners
		 */
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "pgf:main");
		getServer().getMessenger().registerIncomingPluginChannel(this, "pgf:main", this);

		getServer().getPluginManager().registerEvents(this, this);
		//**
		
		/**
		 * Register Commands
		 */
		getServer().getPluginCommand("connect").setExecutor(new ConnectCommand());
		
		/**
		 * Initialize and loop methods
		 */
		loopForPluginMessages();
		
	}
	
	@Override
	public void onDisable () {}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		final Player player = e.getPlayer();
		
		if (player == null || !player.isOnline()) return;
		
		final PlayerData playerdata = PlayerData.from(player);
		
		final StringBuilder builder = new StringBuilder();
		builder.append(ChatColor.RED + "[Attention]\n");
		builder.append(ChatColor.LIGHT_PURPLE + "The " + ChatColor.YELLOW + "\"pgfmc.net\"" + ChatColor.LIGHT_PURPLE + " server address is being replaced\n");
		builder.append("with " + ChatColor.YELLOW + "\"play.pgfmc.net\"" + ChatColor.LIGHT_PURPLE + ". Please switch to the new address.\n");
		builder.append("Thank you!");
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 2F);
				playerdata.sendMessage(builder.toString());
				
			}
		
		}, 20*5); // run after 5 seconds
		
	}

	/**
	 * Plugin Message listener
	 */
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		Logger.debug("Received a plugin message: ");
		Logger.debug("Identifier: " + channel);
		Logger.debug("Player: " + player.getName());
		
		/**
		 * BungeeCord (bungeecord:main) is a Channel Identifier
		 * that is automatically handled by Velocity.
		 * 
		 * ------------------
		 * 
		 * [GetServers] Response
		 * 
		 * Returns a csv list of the registered servers on the proxy
		 * 
		 * PLUGIN MESSAGE FORM (BungeeCord): GetServers, CSV server names
		 */
		if (channel.equals("BungeeCord") || channel.equals("bungeecord:main"))
		{
			final ByteArrayDataInput in = ByteStreams.newDataInput(message);
			final String subchannel = in.readUTF();
			
			if (subchannel.equals("GetServers"))
			{				
				final String serverNamesCSV = in.readUTF();
				
				Logger.debug("Subchannel: " + subchannel);
				Logger.debug("CSV Server Names: " + serverNamesCSV);
				
				// CSV to Array
				final String[] servers = serverNamesCSV.toLowerCase().split(", ");
				final Map<String, Boolean> newRegisteredServers = new HashMap<>();
				
				for (final String server : servers)
				{
					newRegisteredServers.put(server, registeredServers.getOrDefault(server, Boolean.TRUE));
				}
				
				/**
				 * Clear and re-add servers in case the
				 * registered servers in the velocity.toml changed.
				 */
				registeredServers.clear();
				registeredServers.putAll(newRegisteredServers);
				
				return;
			}
			
			if (subchannel.equals("GetServer"))
			{
				final String serverName = in.readUTF();
				
				Logger.debug("This Server Name: " + serverName);
				
				thisServerName = serverName;
				
				return;
			}
			
			
			
			return;
		}
		
		if (!channel.equals("pgf:main")) return;
		
		final PlayerData playerdata = PlayerData.from(player);
		final ByteArrayDataInput in = ByteStreams.newDataInput(message);
		final String subchannel = in.readUTF();
		
		Logger.debug("Subchannel: " + subchannel);
		
		/**
		 * [ConnectReponse]
		 * 
		 * The ConnectResponse subchannel is used for responding to
		 * the Connect subchannel. It says if the connection attempt was successful or not.
		 * 
		 * PLUGIN MESSAGE FORM (pgf:main): ConnectResponse, <server name>, <true/false>
		 */
		if (subchannel.equals("ConnectResponse"))
		{
			if (player == null || !player.isOnline())
			{
				Logger.warn("Received invalid form in subchannel: ConnectResponse");
				Logger.warn("PLUGIN MESSAGE FORM (pgf:main): ConnectResponse, <attempted server name>, <True/False>");
				
				return;
			}
			
			final String attemptedServerName = in.readUTF();
			final String resultString = in.readUTF();
			final boolean result = Boolean.parseBoolean(resultString);
			
			if (result)
			{
				Logger.debug("Successfully connected " + " to server: " + attemptedServerName);
				
				player.sendMessage(ChatColor.GREEN + "Poof!");
				playerdata.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 1.0F);
			} else
			{
				Logger.warn("This is the value of <True/False>: " + resultString);
				Logger.warn("Failed to connect to server: " + attemptedServerName);
				
				player.sendMessage(ChatColor.RED + "Failed to connect to " + attemptedServerName + ".");
				playerdata.playSound(Sound.ENTITY_VILLAGER_NO);
			}
			
			return;
		}
		
		/**
    	 * [PingServerResponse]
    	 * 
    	 * The PingServerResponse says if the proxy could ping the specified server.
    	 * 
    	 * PLUGIN MESSAGE FORM (pgf:main): PingServerResponse, <server name>, <true/false>
    	 */
		if (subchannel.equals("PingServerResponse"))
		{
			final String serverName = in.readUTF();
			final Boolean isOnline = Boolean.parseBoolean(in.readUTF());
			
			Logger.debug("Server Name: " + serverName);
			Logger.debug("Online: " + isOnline);
			
			if (serverName == null || isOnline == null) return;
			
			registeredServers.put(serverName, isOnline);
			
			return;
		}		
		
	}
	
	/**
	 * A loop for operations that require repeatedly
	 * sending Plugin Messages to the proxy.
	 */
	private final void loopForPluginMessages()
	{
		/**
		 * Used to randomly select an online player to send the Plugin Message through.
		 */
		final Random random = new Random(System.currentTimeMillis());
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			
			@Override
			public void run() {
				final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
				
				/**
				 * The server can only connect to the proxy through proxied players.
				 */
				if (onlinePlayers.isEmpty()) return;
				
				// The randomly selected online player to send the Plugin Message through
				final Player player = (Player) onlinePlayers.toArray()[random.nextInt(onlinePlayers.size())];
				
				getServers(); // Doesn't require a player, but cannot receive the response without an online player
				pingServer(player); // Doesn't require a player, but cannot receive the response without an online player
				
				// Don't need to check this server's name again
				// *Might* change during runtime, but eh..
				if (thisServerName == null || thisServerName.isEmpty())
				{
					getServer(player);
				}
				
			}
			
			/**
			 * [GetServers]
			 * 
			 * Use Bungeecord channel to ask for a list of servers. Response
			 * is handled in the plugin message received listener (in Main).
			 * 
			 * PLUGIN MESSAGE FORM (BungeeCord): GetServers
			 * 
			 */
			private final void getServers()
			{
				final ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("GetServers");
				
				Bukkit.getServer().sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
				
			}
			
			/**
			 * Updates onlineServers for use throughout the plugin
			 * by checking the ping of each registered server.
			 * 
			 * [PingServer]
			 * 
			 * The PingServer subchannel is used for checking if a server is online and reachable by the proxy.
	    	 * 
	    	 * PLUGIN MESSAGE FORM (pgf:main): PingServer, <server name>
			 */
			private final void pingServer(final Player player)
			{
				for (final String server : Main.registeredServers.keySet())
				{
					final ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("PingServer");
					out.writeUTF(server);
					
					player.sendPluginMessage(Main.plugin, "pgf:main", out.toByteArray());
					
				}
				
			}
			
			/**
			 * [GetServer]
			 * 
			 * Gets the server the player is connected to (this server)
			 * 
			 * PLUGIN FORM (BungeeCord): GetServer
			 * 
			 * @param player
			 */
			private final void getServer(final Player player)
			{
				final ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("GetServer");
				
				player.sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
				
			}
			
		}, 200L, 200L); // 10 seconds
		
	}

}
