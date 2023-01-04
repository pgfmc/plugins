package net.pgfmc.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.luckperms.api.LuckPerms;
import net.pgfmc.core.api.inventory.extra.InventoryPressEvent;
import net.pgfmc.core.api.playerdata.PlayerDataManager;
import net.pgfmc.core.api.playerdata.cmd.DumpCommand;
import net.pgfmc.core.api.playerdata.cmd.PlayerDataSetCommand;
import net.pgfmc.core.api.playerdata.cmd.TagCommand;
import net.pgfmc.core.api.request.RequestEvents;
import net.pgfmc.core.api.teleport.SpawnProtect;
import net.pgfmc.core.bot.Bot;
import net.pgfmc.core.cmd.RealName;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.util.files.Configify;
import net.pgfmc.core.util.files.Mixins;
import net.pgfmc.core.util.files.ReloadConfigify;

/**
 * @author bk and CrimsonDart
 */
public class CoreMain extends JavaPlugin {
	
	public static String configPath;
	public static String PlayerDataPath;
	public static final String currentSeason = "Season 10";
	public static final String homeDir = "C:" + File.separator + "Users" + File.separator + "pgfmc"
			+ File.separator + "PGF" + File.separator;
	// "Print Working Directory" gets the working directory of the server
	public static String pwd;
	public static String backupDir;
	
	public static CoreMain plugin;
	//public static Scoreboard scoreboard;
	
	public static LuckPerms luckPermsAPI;
	
	private Bot BOT;
	
	public enum PGFPlugin {
		CLAIMS,
		CORE,
		MODTOOLS,
		SURVIVAL;
		
		private boolean enabled = true;
		
		public void enable()
		{
			enabled = true;
		}
		
		public void disable()
		{
			enabled = false;
		}
		
		public boolean isEnabled()
		{
			return enabled;
		}
		
		public Plugin getPlugin()
		{
			return Bukkit.getPluginManager().getPlugin("PGF-" + name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1));
		}
	}
	
	/**
	 * creates all files, loads all worlds, PlayerData, commands and events.
	 * @author bk
	 */
	@Override
	public void onEnable()
	{
		// defines all constants for the plugin
		plugin = this;
		
		pwd = plugin.getServer().getWorldContainer().getAbsolutePath();
		configPath = plugin.getDataFolder() + File.separator + "config.yml";
		PlayerDataPath = plugin.getDataFolder() + File.separator + "playerData";
		backupDir =  homeDir + "Backups" + File.separator
				+ "Main" + File.separator + currentSeason
				+ File.separator;
		
		
		// makes sure all files exist
		Mixins.getFile(configPath);
		new File(PlayerDataPath).mkdirs();
		
		// Register the LuckPerms API
		RegisteredServiceProvider<LuckPerms> lpProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (lpProvider != null) {
		    LuckPerms lpAPI = lpProvider.getProvider();
		    luckPermsAPI = lpAPI;
		    
		}
		
		// loads PlayerData
		
		PlayerDataManager.setInit(pd -> pd.setData("Name", pd.getName()).queue());
		
		PlayerDataManager.setInit(pd -> {
			
			Map<String, Location> homes = new HashMap<>();
			FileConfiguration db = pd.loadFile();
			
			if (db == null) return;
		
			ConfigurationSection config = db.getConfigurationSection("homes");
			
			if (config != null)
			{
				config.getKeys(false).forEach(home -> {
					homes.put(home, config.getLocation(home));
				});
			}
			
			pd.setData("homes", homes);
		});
		
		PlayerDataManager.setInit(pd -> {
			
			FileConfiguration db = pd.loadFile();
			
			if (db == null) return;
			
			pd.setData("nick", db.getString("nick"));
		});
		
		// Makes it so you can /<world> if you want instead of /goto ((	PROBABLY DOESN'T WORK ))
		// getCommand("goto").setAliases(DimManager.getAllWorldNames()); // /hub, /creative, /<world>
		
		
		// getCommand("block").setExecutor(new Blocked()); // Breaks the server
		
		getCommand("nick").setExecutor(new Nick());
		
		new Skull("skull");
		
		getCommand("pgf").setExecutor(new ReloadConfigify());
		
		new RealName("realname");
		
		new DumpCommand();
		new TagCommand();
		new PlayerDataSetCommand();
		
		getServer().getPluginManager().registerEvents(new InventoryPressEvent(), this);
		getServer().getPluginManager().registerEvents(new PlayerDataManager(), this);
		getServer().getPluginManager().registerEvents(new SpawnProtect(), this);
		getServer().getPluginManager().registerEvents(new RequestEvents(), this);
		
		new RestartManager();
		
		Plugin pluginCoreProtect = plugin.getServer().getPluginManager().getPlugin("CoreProtect");
		CoreProtectAPI coreProtectAPI = ((CoreProtect) pluginCoreProtect).getAPI();
		
		if (coreProtectAPI != null) { coreProtectAPI.performPurge(1209600); } // 14 days in seconds
		
		BOT = new Bot();
		
	}
	
	@Override
	public void onDisable() {
		BOT.shutdown();
		PlayerDataManager.saveQ();
		Configify.disableConfigify();
	}
	
	@EventHandler
	public void onLoad(ServerLoadEvent e) {
		PlayerDataManager.initializePD();	
		
		Configify.enableConfigify();
	}
	
}
