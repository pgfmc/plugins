package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.balance.ItemProtect;
import net.pgfmc.survival.balance.PvPEvent;
import net.pgfmc.survival.cmd.Afk;
import net.pgfmc.survival.cmd.Back;
import net.pgfmc.survival.cmd.GiveLodestoneCommand;
import net.pgfmc.survival.cmd.PvpToggle;
import net.pgfmc.survival.cmd.donator.Craft;
import net.pgfmc.survival.cmd.donator.Echest;
import net.pgfmc.survival.cmd.home.DelHome;
import net.pgfmc.survival.cmd.home.Home;
import net.pgfmc.survival.cmd.home.Homes;
import net.pgfmc.survival.cmd.home.SetHome;
import net.pgfmc.survival.cmd.tpa.TpHereRequest;
import net.pgfmc.survival.cmd.tpa.TpRequest;
import net.pgfmc.survival.cmd.warp.DelWarp;
import net.pgfmc.survival.cmd.warp.SetWarp;
import net.pgfmc.survival.cmd.warp.Warp;
import net.pgfmc.survival.cmd.warp.Warps;
import net.pgfmc.survival.masterbook.MasterbookBook;
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
		new GiveLodestoneCommand("getClaim");
		
		new PvpToggle();
		
		
		getServer().getPluginManager().registerEvents(new NicknameInput(), this);
		getServer().getPluginManager().registerEvents(new MasterbookBook(), this);
		
		getServer().getPluginManager().registerEvents(new Afk(), this);
		getServer().getPluginManager().registerEvents(new ItemProtect(), this);
		getServer().getPluginManager().registerEvents(new PvPEvent(), this);
		
		new Warp("warp");
		getCommand("warps").setExecutor(new Warps());
		getCommand("setwarp").setExecutor(new SetWarp());
		getCommand("delwarp").setExecutor(new DelWarp());
		
		Back back = new Back("back");
		getServer().getPluginManager().registerEvents(back, this);
		
	}
	
	@Override
	public void onDisable()
	{
		
	}	
}
