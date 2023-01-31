package net.pgfmc.survival;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.balance.ItemProtect;
import net.pgfmc.survival.cmd.Back;
import net.pgfmc.survival.cmd.GetClaim;
import net.pgfmc.survival.cmd.afk.Afk;
import net.pgfmc.survival.cmd.afk.AfkEvents;
import net.pgfmc.survival.cmd.donator.Craft;
import net.pgfmc.survival.cmd.donator.Echest;
import net.pgfmc.survival.cmd.home.DelHome;
import net.pgfmc.survival.cmd.home.Home;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.cmd.home.SetHome;
import net.pgfmc.survival.cmd.pvp.Pvp;
import net.pgfmc.survival.cmd.pvp.PvpEvent;
import net.pgfmc.survival.cmd.tpa.TpHereRequest;
import net.pgfmc.survival.cmd.tpa.TpRequest;
import net.pgfmc.survival.cmd.warp.DelWarp;
import net.pgfmc.survival.cmd.warp.SetWarp;
import net.pgfmc.survival.cmd.warp.Warp;
import net.pgfmc.survival.cmd.warp.Warps;
import net.pgfmc.survival.masterbook.BookInput;
import net.pgfmc.survival.masterbook.NicknameInput;
import net.pgfmc.survival.masterbook.cmd.Masterbook;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		if (getConfig().getConfigurationSection("warps") == null)
		{
			getConfig().createSection("warps");
			saveConfig();
		}
		
		TpRequest.registerAll();
		TpHereRequest.registerAll();
		
		getCommand("afk").setExecutor(new Afk());
		getCommand("echest").setExecutor(new Echest());
		getCommand("craft").setExecutor(new Craft());
		
		new Home("home");
		new SetHome("sethome");
		new DelHome("delhome");
		new Homes("homes");
		
		new Masterbook("commands");
		new GetClaim("getClaim");
		
		new Pvp();
		
		
		getServer().getPluginManager().registerEvents(new NicknameInput(), this);
		getServer().getPluginManager().registerEvents(new BookInput(), this);
		
		getServer().getPluginManager().registerEvents(new AfkEvents(), this);
		getServer().getPluginManager().registerEvents(new ItemProtect(), this);
		getServer().getPluginManager().registerEvents(new PvpEvent(), this);
		
		new Warp("warp");
		getCommand("warps").setExecutor(new Warps());
		getCommand("setwarp").setExecutor(new SetWarp());
		getCommand("delwarp").setExecutor(new DelWarp());
		
		Back back = new Back("back");
		getServer().getPluginManager().registerEvents(back, this);
		
		startHelpTipsTimer();
		
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	private void startHelpTipsTimer()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				
				PlayerData.getPlayerDataSet(pd -> pd.isOnline()).stream().forEach(pd -> {
					
					if (!pd.hasTag("tip-masterbook")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "Use \"/c\" to open the Command Menu" + "\n"
								+ "You can access common commands here");
						pd.addTag("tip-masterbook");
						
					} else if (!pd.hasTag("tip-discord")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "Join the Discord server!" + "\n" + ChatColor.BLUE + ChatColor.UNDERLINE
								+ "https://discord.gg/zdxeREe");
						pd.addTag("tip-discord");
						
					} else if (!pd.hasTag("tip-link")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "You can link your Discord account to your Minecraft account" + "\n"
								+ "to unlock special features (and more for donators!)");
						pd.addTag("tip-link");
						
					} else if (!pd.hasTag("tip-donatorad")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "By donating to the server, you can unlock special perks." + "\n"
								+ "You can donate by joining the Discord (thanks!)");
						pd.addTag("tip-donatorad");
						
					} else if (!pd.hasTag("tip-afk")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "You can enable AFK mode to become invulnerable");
						pd.addTag("tip-afk");
						
					} else if (!pd.hasTag("tip-home")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "You can set and travel to homes");
						pd.addTag("tip-home");
						
					} else if (!pd.hasTag("tip-tpa")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "You can send teleport requests to other players");
						pd.addTag("tip-tpa");
						
					} else if (!pd.hasTag("tip-claim")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "Use loadstones to claim land! Add players to your claim");
						pd.addTag("tip-claim");
						
					} else if (!pd.hasTag("tip-back")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "You can teleport back to your last known location" + "\n"
								+ "E.g. Death location, teleport location");
						pd.addTag("tip-back");
						
					} else if (!pd.hasTag("tip-protectitems")) {
						pd.sendMessage(ChatColor.GRAY + "<" + ChatColor.RED + ChatColor.GRAY + "> " + ChatColor.GOLD
								+ "Items are protected for 2 minutes apon death");
						pd.addTag("tip-protectitems");
						
					}
						
					
				});
				
			}
		}, 20L * 60L, 20L * 60L * 10L); // Delayed 1 minute, repeat every 10 minutes
		
	}
	
}
