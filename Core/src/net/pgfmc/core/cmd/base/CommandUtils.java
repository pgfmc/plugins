package net.pgfmc.core.cmd.base;

import org.bukkit.command.CommandSender;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class CommandUtils {
	
	public static PlayerData parsePlayerName(String name, CommandSender sender) {
		
		if (name == null) {
			sender.sendMessage("§cPlease enter a player name.");
			return null;
		}
		
		PlayerData pd = PlayerData.from(name);
		
		if (pd == null) {
			sender.sendMessage("§cInvalid player name.");
			return null;
		}
		
		return pd;
	}
	
	
	
	
	
}
