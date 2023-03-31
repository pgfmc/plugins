package net.pgfmc.modtools.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

public class Debug implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		PlayerData p = PlayerData.from((Player) sender);
		
		if (p != null)
		{
			boolean t = p.hasTag("debug");

			if (t) {
				p.sendMessage(ChatColor.GOLD + "Debug disabled!");
				p.setDebug(false);
				
			} else {
				p.sendMessage(ChatColor.GOLD + "Debug enabled!");
				p.setDebug(true);
				
			}
			
		}
		
		return true;
	}
	
}
