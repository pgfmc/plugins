package net.pgfmc.modtools.rollback.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Rollback implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) return false;
		
		if (!args[0].equals("undo")) return new RollbackInventory().onCommand(sender, cmd, label, args);
		if (args[0].equals("undo")) return new UndoRollbackInventory().onCommand(sender, cmd, label, args);
		
		return false;
	}

}
