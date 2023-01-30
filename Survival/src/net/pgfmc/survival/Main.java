package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

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
		
	}
	
	@Override
	public void onDisable()
	{
		
	}	
}
