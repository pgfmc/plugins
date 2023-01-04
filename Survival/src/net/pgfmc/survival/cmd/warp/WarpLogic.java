package net.pgfmc.survival.cmd.warp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import net.pgfmc.survival.Main;

public class WarpLogic {
	
	public static Location getWarp(String name)
	{
		name = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		ConfigurationSection config = Main.plugin.getConfig().getConfigurationSection("warps");
		return config.getLocation(name);
	}
	
	public static Map<String, Location> getWarps() {
		
		ConfigurationSection config = Main.plugin.getConfig().getConfigurationSection("warps");
		
		Map<String, Location> map = new HashMap<>();
		
		for (String s : config.getKeys(false)) {
			map.put(s, config.getLocation(s));
		}
		
		return map;
	}
	
	public static Set<String> getWarpNames() {
		ConfigurationSection config = Main.plugin.getConfig().getConfigurationSection("warps");
		return config.getKeys(false);
	}
	
	public static boolean setWarp(String name, Location loc) {
		if (getWarp(name) != null) return false;
		
		ConfigurationSection config = Main.plugin.getConfig().getConfigurationSection("warps");
		
		config.set(name, loc);
		Main.plugin.saveConfig();
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
}
