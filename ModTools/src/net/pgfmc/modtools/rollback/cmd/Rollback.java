package net.pgfmc.modtools.rollback.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.modtools.rollback.inv.RollbackOnlinePlayersListInventory;

public class Rollback implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("pgf.admin.rollbackinventory"))
		{
			p.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
			return true;
		}
		
		p.openInventory(new RollbackOnlinePlayersListInventory().getInventory());
		
		return true;
	}

}
