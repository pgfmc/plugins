package net.pgfmc.modtools.rollback.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.rollback.inv.RollbackListInventory;

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
		
		if (args.length < 1)
		{
			p.sendMessage(ChatColor.RED + "Please include a player.");
			return true;
		}
		
		if (args.length > 1)
		{
			p.sendMessage(ChatColor.RED + "Usage: /rollback <player>");
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null)
		{
			p.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
			return true;
		}
		
		p.openInventory(new RollbackListInventory(PlayerData.from(target)).getInventory());
		p.sendMessage(ChatColor.GREEN + "Select an inventory instance for " + args[0] + ".");
		
		return true;
	}

}
