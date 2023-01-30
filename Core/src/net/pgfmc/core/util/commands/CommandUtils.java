package net.pgfmc.core.util.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.api.playerdata.PlayerData;

public class CommandUtils {
	
	public static PlayerData parsePlayerName(String name, CommandSender sender) {
		
		if (name == null) {
			sender.sendMessage(ChatColor.RED + "Please enter a player name.");
			return null;
		}
		
		@SuppressWarnings("deprecation")
		PlayerData pd = PlayerData.from(name);
		
		if (pd == null) {
			sender.sendMessage(ChatColor.RED + "Invalid player name.");
			return null;
		}
		
		return pd;
		
	}
	
}
