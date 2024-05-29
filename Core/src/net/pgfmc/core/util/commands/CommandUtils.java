package net.pgfmc.core.util.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

public class CommandUtils {
	
	public static PlayerData parsePlayerName(String name, CommandSender sender) {
		
		if (name == null) {
			sender.sendMessage(ChatColor.RED + "Please enter a player name.");
			return null;
		}
		
		final Player player = Bukkit.getPlayer(name);
		
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Invalid player name.");
			return null;
		}
		
		PlayerData pd = PlayerData.from(player);
		return pd;
		
	}
	
}
