package net.pgfmc.proxycore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.proxycore.listeners.ConnectResponse;
import net.pgfmc.proxycore.serverselector.ConnectCommand;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.pluginmessage.PluginMessageType;
import net.pgfmc.proxycore.util.pluginmessage.test.TestPluginMessageCommand;

public class Main extends JavaPlugin {
	
	private static String thisServerName = null;
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
		new ConnectResponse();
		new TestPluginMessageCommand("testpluginmessage");
		//**
		
		/**
		 * Register Commands
		 */
		getServer().getPluginCommand("connect").setExecutor(new ConnectCommand());
		//**
		
		/**
		 * Initialize methods and loops
		 */
		loopForPluginMessages();
		//**
	}
	
	@Override
	public void onDisable () {}
	
	public static String getServerName()
	{
		return thisServerName;
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
				
				getServers(player); // Doesn't require a player, but cannot receive the response without an online player
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
			private final void getServers(final Player player)
			{
				PluginMessageType.GET_SERVERS.send(player)
					.whenComplete((args, exception) -> {
						if (exception != null)
						{
							Logger.error("Exception occurred for plugin message GET_SERVERS:");
							exception.printStackTrace();
							
							return;
						}
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
						final String serverNamesCSV = args.get(1);
						
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
						
					});
				
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
					PluginMessageType.PING_SERVER.send(player, server)
						.whenComplete((args, exception) -> {
							if (exception != null)
							{
								Logger.error("Exception occurred for plugin message PING_SERVER:");
								exception.printStackTrace();
								
								return;
							}
							/**
					    	 * [PingServerResponse]
					    	 * 
					    	 * The PingServerResponse says if the proxy could ping the specified server.
					    	 * 
					    	 * PLUGIN MESSAGE FORM (pgf:main): PingServerResponse, <server name>, <true/false>
					    	 */
							final String serverName = args.get(1);
							final Boolean isOnline = Boolean.parseBoolean(args.get(2));
							
							Logger.debug("Server Name: " + serverName);
							Logger.debug("Online: " + isOnline);
							
							if (serverName == null || isOnline == null) return;
							
							registeredServers.put(serverName, isOnline);
							
						});
					
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
				PluginMessageType.GET_SERVER.send(player)
					.whenComplete((args, exception) -> {
						if (exception != null)
						{
							Logger.error("Exception occurred for plugin message GET_SERVER:");
							exception.printStackTrace();
							
							return;
						}
						
						final String serverName = args.get(1);
						
						Logger.debug("This Server Name: " + serverName);
						
						thisServerName = serverName;
						
					});
				
			}
			
		}, 200L, 200L); // 10 seconds
		
	}

}
