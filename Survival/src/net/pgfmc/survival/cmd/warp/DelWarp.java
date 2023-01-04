package net.pgfmc.survival.cmd.warp;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.survival.Main;


public class DelWarp implements CommandExecutor {

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
		
		String name = args[0].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		FileConfiguration config = Main.plugin.getConfig();
		
		List<Map<?, ?>> warps = config.getMapList("warps");
		Map<?, ?> warp = Warps.getWarp(name);
		
		if (warp == null)
		{
			sender.sendMessage("§cCould not find warp: §6" + name);
			return true;
		}
		
		warps.remove(warp);
		sender.sendMessage("§aRemoved warp: §6" + name);
		
		config.set("warps", warps);
		Main.plugin.saveConfig();
		
		return true;
	}

}
