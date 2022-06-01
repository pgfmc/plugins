package net.pgfmc.ffa;

import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.ffa.cmd.SetZoneInventory;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		getCommand("setzoneinventory").setExecutor(new SetZoneInventory());
	}
	
	@Override
	public void onDisable()
	{
		
	}

}
