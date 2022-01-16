package net.pgfmc.teleport.warp;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.teleport.Main;

public class SetWarp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		if (args.length != 1)
		{
			return false;
		}
		
		Player p = (Player) sender;
		
		String name = args[0].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		if (Warps.getWarp(name) != null)
		{
			p.sendMessage("§cCannot have duplicate warp names: §6" + name);
			return true;
		}
		
		FileConfiguration config = Main.plugin.getConfig();
		
		List<Map<?, ?>> warps = config.getMapList("warps");
		
		warps.add(Map.of(name, p.getLocation()));
		
		p.sendMessage("§aSet new warp: §6" + name);
		
		config.set("warps", warps);
		Main.plugin.saveConfig();
		
		return true;
	}

}
