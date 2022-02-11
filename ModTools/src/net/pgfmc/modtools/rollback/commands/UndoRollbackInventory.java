package net.pgfmc.modtools.rollback.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.modtools.rollback.PlayerInventorySaver;

public class UndoRollbackInventory {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 2) return false;
		
		PlayerData target = PlayerData.from(args[1]);
		
		if (target == null)
		{
			sender.sendMessage(ChatColor.RED + "Could not find player " + args[1] + ".");
			return true;
		}
		
		PlayerInventorySaver.undo(target);
		sender.sendMessage(ChatColor.GREEN + "Successfully undid rollback inventory for " + target.getRankedName() + "!");
		target.sendMessage(ChatColor.GREEN + "Your inventory rollback has been undone!");
		
		return true;
	}
	
}
