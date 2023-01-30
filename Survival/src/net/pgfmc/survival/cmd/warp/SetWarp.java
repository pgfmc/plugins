package net.pgfmc.survival.cmd.warp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.pgfmc.survival.Main;

public class SetWarp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		if (args.length != 1)
		{
			return false;
		}
		
		Player p = (Player) sender;
		
		String name = args[0].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		if (WarpLogic.getWarp(name) != null)
		{
			p.sendMessage(ChatColor.RED + "Cannot have duplicate warp names: " + ChatColor.GOLD + name);
			return true;
		}
		
		ConfigurationSection config = Main.plugin.getConfig().getConfigurationSection("warps");
		
		config.set(name, p.getLocation());
		
		p.sendMessage(ChatColor.GREEN + "Set new warp: " + ChatColor.GOLD + name);
		
		Main.plugin.saveConfig();
		
		return true;
	}

}
