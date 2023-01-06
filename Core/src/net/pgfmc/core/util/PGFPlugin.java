package net.pgfmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public enum PGFPlugin {
	CLAIMS,
	CORE,
	MODTOOLS,
	SURVIVAL;
	
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
		return Bukkit.getPluginManager().getPlugin("PGF-" + name().toUpperCase().substring(0, 1) + name().toLowerCase().substring(1));
	}
	
}
