package net.pgfmc.survival.cmd.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
		
		ConfigurationSection config = Main.plugin.getConfig().getConfigurationSection("warps");
		
		config.set(name, null); // Completely deletes it
		
		sender.sendMessage("§aRemoved warp: §6" + name);
		
		Main.plugin.saveConfig();
		
		return true;
	}

}
