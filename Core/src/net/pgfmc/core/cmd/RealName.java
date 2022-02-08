package net.pgfmc.core.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class RealName implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (label.length() == 0) return false;
		
		PlayerData pd = PlayerData.getPlayerData(args[0]);
		
		if (pd == null)
		{
			sender.sendMessage(ChatColor.GOLD + "Could not find real name for " + args[0]);
			return true;
		}
		
		sender.sendMessage(ChatColor.GOLD + "Real name for " + pd.getRankedName() + " is " + pd.getRankColor() + pd.getName());
		
		return true;
	}

}
