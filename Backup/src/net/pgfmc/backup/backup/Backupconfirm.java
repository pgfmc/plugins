package net.pgfmc.backup.backup;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Backupconfirm implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String uuid = "Console";
		if (sender instanceof Player) { uuid = ((Player) sender).getUniqueId().toString(); }
		
		
		/*
		 * Searches for a Backup object with the same UUID
		 * Uses that object for the backup info
		 * 
		 * Does not start a backup if no matches are found
		 */
		for (Backup b : Backup.BACKUPS)
		{
			if (b.backup.get("uuid").equals(uuid))
			{
				sender.sendMessage("§6Creating backup as §f§o" + b.date
						+ "§r§6.\nThe server will restart once complete.");
				b.backup();
				
				return true;
			}
		}
		
		// idc
		
		return true;
	}

}
