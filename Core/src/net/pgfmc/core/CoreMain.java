package net.pgfmc.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.cmd.Goto;
import net.pgfmc.core.cmd.RealName;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.file.Configify;
import net.pgfmc.core.file.Mixins;
import net.pgfmc.core.file.ReloadConfigify;
import net.pgfmc.core.inventoryAPI.extra.InventoryPressEvent;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;
import net.pgfmc.core.playerdataAPI.cmd.DumpCommand;
import net.pgfmc.core.playerdataAPI.cmd.PlayerDataSetCommand;
import net.pgfmc.core.playerdataAPI.cmd.TagCommand;
import net.pgfmc.core.requests.RequestEvents;
import net.pgfmc.core.teleportAPI.SpawnProtect;
import net.pgfmc.core.util.DimManager;

/**
 * @author bk and CrimsonDart
 */
public class CoreMain extends JavaPlugin implements Listener {
	
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
	
	public enum PGFPlugin {
		BACKUP,
		BOT,
		CLAIMS,
		CORE,
		DUELS,
		FRIENDS,
		MARKET,
		MASTERBOOK,
		MODTOOLS,
		SURVIVAL,
		TELEPORT;
		
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
			return Bukkit.getPluginManager().getPlugin("PGF-" + this.name());
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
		
		DimManager.updateConfigForWorldPermissionAccess();
		
		getCommand("goto").setExecutor(new Goto());
		
		// Makes it so you can /<world> if you want instead of /goto ((	PROBABLY DOESN'T WORK ))
		// getCommand("goto").setAliases(DimManager.getAllWorldNames()); // /hub, /creative, /<world>
		
		
		// getCommand("block").setExecutor(new Blocked()); // Breaks the server
		
		getCommand("nick").setExecutor(new Nick());
		
		getCommand("skull").setExecutor(new Skull());
		
		getCommand("pgf").setExecutor(new ReloadConfigify());
		
		getCommand("realname").setExecutor(new RealName());
		
		new DumpCommand();
		new TagCommand();
		new PlayerDataSetCommand();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getServer().getPluginManager().registerEvents(new InventoryPressEvent(), this);
		getServer().getPluginManager().registerEvents(new PlayerDataManager(), this);
		getServer().getPluginManager().registerEvents(new Permissions(), this);
		getServer().getPluginManager().registerEvents(new SpawnProtect(), this);
		getServer().getPluginManager().registerEvents(new RequestEvents(), this);
		
		new ProfanityFilter();
	}
	
	@Override
	public void onDisable() {
		PlayerDataManager.saveQ();
		Permissions.clear();
		Configify.disableConfigify();
	}
	
	
	@EventHandler
	public void onLoad(ServerLoadEvent e) {
		
		String ver = getDescription().getVersion();		
		
		for (PGFPlugin p : PGFPlugin.values())
		{
			Plugin pl = p.getPlugin();
			if (pl == null)
			{
				Bukkit.getLogger().severe("[PGF-" + p.name() + "] is disabled!");
				p.disable();
				continue;
			}
			
			if (!pl.getDescription().getVersion().equals(ver))
			{
				Bukkit.getLogger().warning("[" + pl.getName() + " (" + pl.getDescription().getVersion() + ")] does not match PGF-Core (" + ver + ") version!");
			}
		}
		
		Configify.enableConfigify();
	}
}

