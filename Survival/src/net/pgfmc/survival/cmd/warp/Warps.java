package net.pgfmc.survival.cmd.warp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Warps implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		sender.sendMessage(ChatColor.GOLD + "6Warps: " + String.join(", ", WarpLogic.getWarpNames()));
		
		return true;
	}

}
