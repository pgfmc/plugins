package net.pgfmc.survival;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.playerdata.PlayerDataManager;
import net.pgfmc.core.util.files.Mixins;
import net.pgfmc.survival.cmd.Back;
import net.pgfmc.survival.cmd.afk.Afk;
import net.pgfmc.survival.cmd.afk.AfkEvents;
import net.pgfmc.survival.cmd.donator.Craft;
import net.pgfmc.survival.cmd.donator.Echest;
import net.pgfmc.survival.cmd.home.DelHome;
import net.pgfmc.survival.cmd.home.Home;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.cmd.home.SetHome;
import net.pgfmc.survival.cmd.menu.CommandMenu;
import net.pgfmc.survival.cmd.menu.CommandMenuBookInput;
import net.pgfmc.survival.cmd.pvp.Pvp;
import net.pgfmc.survival.cmd.pvp.PvpEvent;
import net.pgfmc.survival.cmd.tpa.TpHereRequest;
import net.pgfmc.survival.cmd.tpa.TpRequest;
import net.pgfmc.survival.cmd.warp.DelWarp;
import net.pgfmc.survival.cmd.warp.SetWarp;
import net.pgfmc.survival.cmd.warp.Warp;
import net.pgfmc.survival.cmd.warp.Warps;
import net.pgfmc.survival.menu.staff.giverewards.GiveRewardsListInventory;
import net.pgfmc.survival.menu.staff.inventorybackups.noninv.InventoryBackup;
import net.pgfmc.survival.menu.staff.inventorybackups.noninv.InventoryBackupScheduler;
import net.pgfmc.survival.particleeffects.HaloEffect;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		// Creates rewards.yml for the player rewards
		// If it doesn't exist
		Mixins.getDatabase(getDataFolder() + File.separator + "rewards.yml");
		Rewards.loadRewardsFile();
		
		// Create warps section in config.yml if it doesn't exist
		if (getConfig().getConfigurationSection("warps") == null)
		{
			getConfig().createSection("warps");
			saveConfig();
		}
		
		// PlayerData Inits
		PlayerDataManager.setInit(playerdata -> {
			final FileConfiguration config = playerdata.getPlayerDataFile();
			
			if (config.get("particle_effect") == null) return;
			
			final String particle = config.getString("particle_effect");
			playerdata.setData("particle_effect", particle);			
			
		});
		
		// Other Inits
		startHelpTipsTimer();
		new HaloEffect();
		// Register tp requests
		TpRequest.registerAll();
		TpHereRequest.registerAll();
	
		// Commands
		getCommand("afk").setExecutor(new Afk());
		getCommand("echest").setExecutor(new Echest());
		getCommand("craft").setExecutor(new Craft());
		getCommand("warps").setExecutor(new Warps());
		getCommand("setwarp").setExecutor(new SetWarp());
		getCommand("delwarp").setExecutor(new DelWarp());
		new Home("home");
		new SetHome("sethome");
		new DelHome("delhome");
		new Homes("homes");
		new CommandMenu("menu");
		new Pvp();
		new Warp("warp");
		new Back("back");
		
		// Listeners
		getServer().getPluginManager().registerEvents(new CommandMenuBookInput(), this);
		getServer().getPluginManager().registerEvents(new AfkEvents(), this);
		getServer().getPluginManager().registerEvents(new PvpEvent(), this);
		getServer().getPluginManager().registerEvents(new GiveRewardsListInventory(), this);
		getServer().getPluginManager().registerEvents(new InventoryBackupScheduler(), this);

	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTask(InventoryBackupScheduler.INVENTORY_ROLLBACK_TASK_ID);
		
		PlayerData.getPlayerDataSet().stream().forEach(pd -> {
			@SuppressWarnings("unchecked")
			List<InventoryBackup> inventories = (List<InventoryBackup>) Optional.ofNullable(pd.getData("inventories")).orElse(new ArrayList<InventoryBackup>());
			
			inventories.stream().forEach(inventory -> {
				Bukkit.getScheduler().cancelTask(inventory.getTaskId());
				
			});
		});
		
		
	}
	
	private void startHelpTipsTimer()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				
				PlayerData.getPlayerDataSet(pd -> pd.isOnline()).stream().forEach(pd -> {
					
					final String prefix = ChatColor.GRAY + "<" + ChatColor.RED + "!"  + ChatColor.GRAY + "> " + ChatColor.GOLD;
					
					if (!pd.hasTag("tip-masterbook")) {
						pd.sendMessage(prefix + "Use \"/c\" to open the Command Menu"
								+ "\n"
								+ "You can access common commands here");
						pd.addTag("tip-masterbook");
						
					} else if (!pd.hasTag("tip-discord")) {
						pd.sendMessage(prefix + "Join the Discord server!"
								+ "\n" + ChatColor.BLUE + ChatColor.UNDERLINE
								+ "https://discord.gg/zdxeREe");
						pd.addTag("tip-discord");
						
					} else if (!pd.hasTag("tip-link")) {
						pd.sendMessage(prefix + "You can link your Discord account to your Minecraft account"
								+ "\n"
								+ "to unlock special features (and more for donators!)");
						pd.addTag("tip-link");
						
					} else if (!pd.hasTag("tip-donatorad")) {
						pd.sendMessage(prefix + "By donating to the server, you can unlock special perks."
								+ "\n"
								+ "You can donate by joining the Discord (thanks!)");
						pd.addTag("tip-donatorad");
						
					} else if (!pd.hasTag("tip-afk")) {
						pd.sendMessage(prefix + "You can enable AFK mode to become invulnerable");
						pd.addTag("tip-afk");
						
					} else if (!pd.hasTag("tip-home")) {
						pd.sendMessage(prefix + "You can set and travel to homes");
						pd.addTag("tip-home");
						
					} else if (!pd.hasTag("tip-tpa")) {
						pd.sendMessage(prefix + "You can send teleport requests to other players");
						pd.addTag("tip-tpa");
						
					} else if (!pd.hasTag("tip-claim")) {
						pd.sendMessage(prefix + "Use loadstones to claim land! Add players to your claim");
						pd.addTag("tip-claim");
						
					} else if (!pd.hasTag("tip-back")) {
						pd.sendMessage(prefix + "You can teleport back to your last known location"
								+ "\n"
								+ "E.g. Death location, teleport location");
						pd.addTag("tip-back");
						
					} else if (!pd.hasTag("tip-protectitems")) {
						pd.sendMessage(prefix + "Items are protected for 2 minutes upon death");
						pd.addTag("tip-protectitems");
						
					}
						
					
				});
				
			}
		}, 20L * 60L, 20L * 60L * 10L); // Delayed 1 minute, repeat every 10 minutes
		
	}
	
}
