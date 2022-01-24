package net.pgfmc.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.ServerLoadEvent.LoadType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.cmd.Goto;
import net.pgfmc.core.cmd.admin.Broadcast;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.cmd.admin.Tagging;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.inventoryAPI.extra.InventoryPressEvent;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;
import net.pgfmc.core.util.DimManager;
import net.pgfmc.core.util.Mixins;
import net.pgfmc.core.util.ReloadConfigify;

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
	public static Scoreboard scoreboard;
	
	public enum PGFPlugin {
		BACKUP,
		BOT,
		CORE,
		MARKET,
		MASTERBOOK,
		MODTOOLS,
		SURVIVAL,
		TEAMS,
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
	
	public enum Machine {
		MAIN,
		TEST,
		JIMBO,
		CRIMSON
	};
	
	public static Machine machine;
	
	/**
	 * creates all files, loads all worlds, PlayerData, commands and events.
	 * @author bk
	 */
	@Override
	public void onEnable()
	{ 
		// defines all constants for the plugin
		plugin = this;
		
		
		pwd = CoreMain.plugin.getServer().getWorldContainer().getAbsolutePath();
		configPath = CoreMain.plugin.getDataFolder() + File.separator + "config.yml";
		PlayerDataPath = CoreMain.plugin.getDataFolder() + File.separator + "playerData";
		backupDir =  homeDir + "Backups" + File.separator
				+ "Main" + File.separator + currentSeason
				+ File.separator;
		
		switch (this.getServer().getPort()) {
		case 25566: machine = Machine.TEST; break;
		case 25567: machine = Machine.JIMBO; break;
		case 25568: machine = Machine.TEST; break;
		case 25569: machine = Machine.CRIMSON; break;
		default: machine = Machine.MAIN; break;
		}
		
		
		// makes sure all files exist
		Mixins.getFile(configPath);
		new File(PlayerDataPath).mkdirs();
		
		// scoreboard stuff
		Scoreboard scorebored = Bukkit.getScoreboardManager().getNewScoreboard();
		scorebored.registerNewTeam("survival");
		scoreboard = scorebored;
		
		// loads PlayerData
		
		PlayerDataManager.setInit(x -> x.setData("AFK", false));
		PlayerDataManager.setInit(pd -> pd.setData("god", false));
		PlayerDataManager.setInit(pd -> pd.setData("fly", false));
		PlayerDataManager.setInit(pd -> pd.setData("vanish", false));
		
		PlayerDataManager.setInit(pd -> pd.setData("Name", pd.getName()).queue());
		
		PlayerDataManager.setInit(pd -> {
			
			Map<String, Location> homes = new HashMap<>();
			FileConfiguration db = pd.loadFile();
			
			if (db == null)
			{
				new Exception("FileConfiguration for PlayerData setInit is null.").printStackTrace();
				return;
			}
		
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
			
			if (db == null) throw new NullPointerException();
			
			pd.setData("nick", Optional.ofNullable(db.getString("nick")).orElse(null));
		});
		
		DimManager.updateConfigForWorldPermissionAccess();
		
		
		
		//checks for the team "survival"
		//if it doesnt exist, it creates a new team with the same name.
		
		getCommand("goto").setExecutor(new Goto());
		
		// Makes it so you can /<world> if you want instead of /goto ((	PROBABLY DOESN'T WORK ))
		// getCommand("goto").setAliases(DimManager.getAllWorldNames()); // /hub, /creative, /<world>
		
		
		// getCommand("block").setExecutor(new Blocked()); // Breaks the server
		
		getCommand("nick").setExecutor(new Nick());
		
		getCommand("skull").setExecutor(new Skull());
		
		getCommand("pgf").setExecutor(new ReloadConfigify());
		getCommand("tag").setExecutor(new Tagging());
		
		getCommand("broadcast").setExecutor(new Broadcast());
		
		
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getServer().getPluginManager().registerEvents(new InventoryPressEvent(), this);
		getServer().getPluginManager().registerEvents(new PlayerDataManager(), this);
		getServer().getPluginManager().registerEvents(new Permissions(), this);
		
		
		System.out.println(Bukkit.getServer().getCommandAliases());
		
		new ProfanityFilter();
		new ReloadConfigify().reload();
	}
	
	@Override
	public void onDisable() {
		PlayerDataManager.saveQ();
		Permissions.clear();
	}
	
	
	@EventHandler
	public void onLoad(ServerLoadEvent e) {
		
		if (e.getType() == LoadType.STARTUP) {
			PlayerDataManager.InitializePD();
		}
		
		String ver = getDescription().getVersion();		
		
		for (PGFPlugin p : PGFPlugin.values())
		{
			Plugin pl = p.getPlugin();
			if (pl == null)
			{
				Bukkit.getLogger().warning("[PGF-" + p.name() + "] is disabled!");
				p.disable();
				continue;
			}
			
			if (!pl.getDescription().getVersion().equals(ver))
			{
				Bukkit.getLogger().warning("[" + pl.getName() + " (" + pl.getDescription().getVersion() + ")] does not match PGF-Core (" + ver + ") version!");
			}
		}
	}
}

