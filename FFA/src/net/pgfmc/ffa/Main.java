package net.pgfmc.ffa;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.ffa.cmd.handler.FFACmdHandler;
import net.pgfmc.ffa.zone.ZoneDetector;
import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.zones.Combat;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("ffa").setExecutor(new FFACmdHandler());
		getServer().getPluginManager().registerEvents(new ZoneDetector(), this);
		getServer().getPluginManager().registerEvents(new Combat(), this);
		
		new ZoneInfo(); // Needed for Configify to work apparently ?
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
