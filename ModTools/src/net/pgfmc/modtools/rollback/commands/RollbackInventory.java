package net.pgfmc.modtools.rollback.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.modtools.rollback.PlayerInventorySaver;

public class RollbackInventory {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 2) return false;
		
		PlayerData target = PlayerData.getPlayerData(args[0]);
		String minutes = args[1].replaceAll("[^0-9]", "");
		
		if (minutes.length() == 0 || Integer.valueOf(minutes) == null || minutes.equals("0")) return false;
		
		if (target == null)
		{
			sender.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
			return true;
		}
		
		PlayerInventorySaver.restore(target, Integer.valueOf(minutes) - 1);
		sender.sendMessage(ChatColor.GREEN + "Successfully rolled back inventory! " + target.getRankedName() + " (" + minutes + " minutes ago)");
		target.sendMessage(ChatColor.GREEN + "Your inventory has been rolledback " + minutes + " minutes!");
		
		return true;
	}

}
