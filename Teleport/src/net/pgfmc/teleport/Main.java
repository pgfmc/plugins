package net.pgfmc.teleport;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.teleport.back.Back;
import net.pgfmc.teleport.home.DelHome;
import net.pgfmc.teleport.home.Home;
import net.pgfmc.teleport.home.Homes;
import net.pgfmc.teleport.home.SetHome;
import net.pgfmc.teleport.tpa.TpHereRequest;
import net.pgfmc.teleport.tpa.TpRequest;
import net.pgfmc.teleport.warp.DelWarp;
import net.pgfmc.teleport.warp.SetWarp;
import net.pgfmc.teleport.warp.Warp;
import net.pgfmc.teleport.warp.Warps;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		TpRequest.registerAll();
		TpHereRequest.registerAll();
		
		getCommand("home").setExecutor(new Home());
		getCommand("sethome").setExecutor(new SetHome());
		getCommand("delhome").setExecutor(new DelHome());
		getCommand("homes").setExecutor(new Homes());
		
		getCommand("warp").setExecutor(new Warp());
		getCommand("warps").setExecutor(new Warps());
		getCommand("setwarp").setExecutor(new SetWarp());
		getCommand("delwarp").setExecutor(new DelWarp());
		
		
		Back back = new Back();
		getCommand("back").setExecutor(back);
		
		getServer().getPluginManager().registerEvents(back, this);
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
