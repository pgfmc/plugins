package net.pgfmc.survival;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.survival.balance.ItemProtect;
import net.pgfmc.survival.balance.PvPEvent;
import net.pgfmc.survival.commands.Afk;
import net.pgfmc.survival.commands.GiveLodestoneCommand;
import net.pgfmc.survival.commands.HelpCommand;
import net.pgfmc.survival.commands.PvpToggle;
import net.pgfmc.survival.commands.donator.Craft;
import net.pgfmc.survival.commands.donator.Echest;
import net.pgfmc.survival.inventories.masterbook.BookClick;
import net.pgfmc.survival.inventories.masterbook.NickHomeInput;
import net.pgfmc.survival.teleport.Back;
import net.pgfmc.survival.teleport.home.DelHome;
import net.pgfmc.survival.teleport.home.Home;
import net.pgfmc.survival.teleport.home.Homes;
import net.pgfmc.survival.teleport.home.SetHome;
import net.pgfmc.survival.teleport.tpa.TpHereRequest;
import net.pgfmc.survival.teleport.tpa.TpRequest;
import net.pgfmc.survival.teleport.warp.DelWarp;
import net.pgfmc.survival.teleport.warp.SetWarp;
import net.pgfmc.survival.teleport.warp.Warp;
import net.pgfmc.survival.teleport.warp.Warps;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		TpRequest.registerAll();
		TpHereRequest.registerAll();
		
		getCommand("afk").setExecutor(new Afk());
		getCommand("echest").setExecutor(new Echest());
		getCommand("craft").setExecutor(new Craft());
		
		new Home("home");
		new SetHome("sethome");
		new DelHome("delhome");
		new Homes("homes");
		
		new HelpCommand("commands");
		new GiveLodestoneCommand("getClaim");
		
		new PvpToggle();
		
		
		getServer().getPluginManager().registerEvents(new NickHomeInput(), this);
		getServer().getPluginManager().registerEvents(new BookClick(), this);
		
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
