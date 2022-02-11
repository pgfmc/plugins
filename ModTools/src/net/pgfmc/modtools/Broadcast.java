package net.pgfmc.modtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.CoreMain;

public class Broadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0)
		{
			return false;
		}
		
		String msg = ChatColor.GRAY + "["
					+ ChatColor.BOLD + ChatColor.LIGHT_PURPLE + "PGF"
					+ ChatColor.GRAY + "] "
					+ ChatColor.RESET + ChatColor.DARK_RED + String.join(" ", args).replace('&', ChatColor.COLOR_CHAR);
		Bukkit.broadcastMessage(msg);
		
		if (!CoreMain.PGFPlugin.BOT.isEnabled()) return true;
		Discord.sendMessage(ChatColor.stripColor(msg));
		
		return true;
	}

}
