package net.pgfmc.ffa;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.ffa.cmd.handler.FFACmdHandler;
import net.pgfmc.ffa.zone.ZoneDetector;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("ffa").setExecutor(new FFACmdHandler());
		getServer().getPluginManager().registerEvents(new ZoneDetector(), this);
		
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
