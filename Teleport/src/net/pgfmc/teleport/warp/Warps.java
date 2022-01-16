package net.pgfmc.teleport.warp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.teleport.Main;


public class Warps implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		sender.sendMessage("§6Warps: " + getWarpNames().toString());
		
		return true;
	}
	
	public static Map<?, ?> getWarp(String name)
	{
		name = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		FileConfiguration config = Main.plugin.getConfig();
		
		List<Map<?, ?>> warps = config.getMapList("warps");
		
		for (Map<?, ?> warp : warps)
		{
			if (warp.containsKey(name))
			{
				return warp;
			}
		}
		
		return null;
	}
	
	public static List<String> getWarpNames()
	{
		List<String> warpNames = new ArrayList<String>();
		
		FileConfiguration config = Main.plugin.getConfig();
		
		List<Map<?, ?>> warps = config.getMapList("warps");
		
		for (Map<?, ?> warp : warps)
		{
			warpNames.add(warp.keySet().toArray()[0].toString());
		}
		
		return warpNames;
	}

}
