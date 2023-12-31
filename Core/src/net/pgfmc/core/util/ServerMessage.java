package net.pgfmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ServerMessage implements CommandExecutor {
	
	private static String MESSAGE_PREFIX = ChatColor.GRAY + "["
											+ ChatColor.BOLD + ChatColor.LIGHT_PURPLE + "PGF"
											+ ChatColor.GRAY + "] "
											+ ChatColor.RESET + ChatColor.DARK_RED;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) return false;
		
		String msg =  MESSAGE_PREFIX + String.join(" ", args).replace('&', ChatColor.COLOR_CHAR);
		
		Bukkit.broadcastMessage(msg);
		
		return true;
	}
	
	public static void sendServerMessage(final String message)
	{
		Bukkit.broadcastMessage(MESSAGE_PREFIX + message);
	}
	
}
