package net.pgfmc.core;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.ServerLoadEvent.LoadType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.pgfmc.core.api.inventory.extra.InventoryPressEvent;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.playerdata.PlayerDataManager;
import net.pgfmc.core.api.playerdata.cmd.DumpCommand;
import net.pgfmc.core.api.playerdata.cmd.PlayerDataSetCommand;
import net.pgfmc.core.api.playerdata.cmd.TagCommand;
import net.pgfmc.core.api.request.RequestEvents;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.teleport.SpawnProtect;
import net.pgfmc.core.bot.minecraft.listeners.OnPlayerAdvancementDone;
import net.pgfmc.core.bot.minecraft.listeners.OnPlayerDeath;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.cmd.serverselector.ConnectCommand;
import net.pgfmc.core.cmd.test.inventory.TestInventorySizeCommand;
import net.pgfmc.core.cmd.test.pluginmessage.TestPluginMessageCommand;
import net.pgfmc.core.listeners.ConnectResponse;
import net.pgfmc.core.listeners.PlayerRoleResponse;
import net.pgfmc.core.listeners.minecraft.OnAsyncPlayerChat;
import net.pgfmc.core.listeners.minecraft.OnPlayerJoin;
import net.pgfmc.core.listeners.minecraft.OnPlayerQuit;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.RestartScheduler;
import net.pgfmc.core.util.ServerMessage;
import net.pgfmc.core.util.proxy.PluginMessageType;

/**
 * @author bk and CrimsonDart
 */
public class CoreMain extends JavaPlugin implements Listener {
	
	/**
	 * Registered servers' names and if they are currently online
	 * 
	 * Online status is updated by Main.loopForPluginMessages()
	 */
	private final static Map<String, Boolean> REGISTERED_SERVERS = new HashMap<>();
	
	public static CoreMain plugin;
	
	private static String thisServerName;
	
	/**
	 * creates all files, loads all worlds, PlayerData, commands and events.
	 * @author bk
	 */
	@Override
	public void onEnable()
	{
		plugin = this;
		
		/**
		 * Create defaults *.yml
		 */
		saveDefaultConfig();
		reloadConfig();
		
		/**
		 * PlayerData initialization
		 */
		PlayerDataManager.setInit(pd -> pd.setData("Name", pd.getName()).queue());
		
		PlayerDataManager.setInit(pd -> {
			final FileConfiguration db = pd.getPlayerDataFile();
			final ConfigurationSection config = db.getConfigurationSection("homes");
			
			if (config == null) return;
			
			Map<String, Location> homes = new HashMap<>();
			
			config.getKeys(false).forEach(home -> {
				final Location homeLocation = config.getLocation(home);
				
				if (homeLocation != null)
				{
					homes.put(home, homeLocation);
				} else
				{
					Bukkit.getLogger().warning("Could not load home for " + pd.getName() + ".");
				}
				
				
			});
			
			if (homes.isEmpty()) return;
			
			pd.setData("homes", homes);
			
		});
		
		PlayerDataManager.setInit(pd -> {
			
			final FileConfiguration db = pd.getPlayerDataFile();
			
			final String nickname = db.getString("nick");
			
			if (nickname == null) return;
			
			pd.setData("nick", nickname);
			
		});
		
		PlayerDataManager.setInit(pd -> {
			PluginMessageType.PLAYER_ROLE.send(CoreMain.plugin.getServer(), pd.getUniqueId().toString());
			
		});
		//
		
		/**
		 * Register commands
		 */		
		getCommand("nick").setExecutor(new Nick());
		getCommand("broadcast").setExecutor(new ServerMessage());
		getCommand("connect").setExecutor(new ConnectCommand());
		new Skull();
		new DumpCommand();
		new TagCommand();
		new PlayerDataSetCommand();
		new TestInventorySizeCommand("testinventorysize");
		//
		
		/**
		 * Register listeners
		 */
		getServer().getPluginManager().registerEvents(new InventoryPressEvent(), this);
		getServer().getPluginManager().registerEvents(new PlayerDataManager(), this);
		getServer().getPluginManager().registerEvents(new SpawnProtect(), this);
		getServer().getPluginManager().registerEvents(new RequestEvents(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerAdvancementDone(), this);
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
		getServer().getPluginManager().registerEvents(this, this);
		new ConnectResponse();
		new PlayerRoleResponse();
		new TestPluginMessageCommand("testpluginmessage");
		//
		
		/**
		 * Initialize classes, loops, methods
		 */
		loopForPluginMessages();
		//
		
	}
	
	@Override
	public void onDisable() {
		PlayerDataManager.saveQ();
		RequestType.saveRequestsToFile();
		
	}
	
	@EventHandler
	public void onLoad(ServerLoadEvent e) {
		if (e.getType() == LoadType.RELOAD) return;
		
		PlayerDataManager.initializePlayerData();
		startRestartThread();
		
		// Purge CoreProtect data of 14 days or older
		Plugin pluginCoreProtect = plugin.getServer().getPluginManager().getPlugin("CoreProtect");
		CoreProtectAPI coreProtectAPI = ((CoreProtect) pluginCoreProtect).getAPI();
		
		if (coreProtectAPI != null) { coreProtectAPI.performPurge(1209600); } // 14 days in seconds
		
	}
	
	private void startRestartThread()
	{
		Calendar now = Calendar.getInstance();
		Calendar restartDate = now;
		
		restartDate.add(Calendar.HOUR, 12 - Math.abs(3 - now.get(Calendar.HOUR))); // Finds how many hours until 3 AM/PM
		restartDate.add(Calendar.MINUTE, -1 * now.get(Calendar.MINUTE));
		restartDate.setTimeZone(TimeZone.getDefault()); // ZonedDateTime from restart date and system's time zone
		
		long secondsUntilRestartCountdown = (Duration.between(Instant.now(), restartDate.toInstant()).getSeconds()) - (60 * 10);  // Calculate amount of time to wait until we run.
		
		Bukkit.getLogger().warning("Restart date: " + secondsUntilRestartCountdown/60/60 + " hours from now.");
		
		new RestartScheduler().runTaskTimer(this, secondsUntilRestartCountdown * 20, 20);
		
	}
	
	public static final String getThisServerName()
	{
		return thisServerName;
	}
	
	public static final Map<String, Boolean> getRegisteredServersMap()
	{
		return REGISTERED_SERVERS;
	}
	
	// Updates all player nameplates so that they appear correct
	// Example: A player changes their nickname: this method should be called
	//			so that other players can correctly see the new nickname
	public static final void updatePlayerNameplate(PlayerData playerdata)
	{
		// Don't do this if the playerdata is offline
		if (!playerdata.isOnline()) return;
		
		final Player player = playerdata.getPlayer();
		
		// Updates playerlist, custom name value (spigot/bukkit), and makes the custom name visible to the CLIENT
		player.setPlayerListName(playerdata.getRankedName());
		player.setCustomName(playerdata.getRankedName());
		player.setCustomNameVisible(true);
		
		// Do this for every player
		for (final Player otherPlayer : Bukkit.getOnlinePlayers())
		{
			// Skip this iteration if the players are the same
			if (otherPlayer == playerdata.getPlayer()) continue;
			
			// Weird way to update the name of a player for other players to see it
			player.hidePlayer(CoreMain.plugin, otherPlayer);
			player.showPlayer(CoreMain.plugin, otherPlayer);
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
							newRegisteredServers.put(server, REGISTERED_SERVERS.getOrDefault(server, Boolean.TRUE));
						}
						
						/**
						 * Clear and re-add servers in case the
						 * registered servers in the velocity.toml changed.
						 */
						REGISTERED_SERVERS.clear();
						REGISTERED_SERVERS.putAll(newRegisteredServers);
						
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
				for (final String server : REGISTERED_SERVERS.keySet())
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
							
							REGISTERED_SERVERS.put(serverName, isOnline);
							
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
