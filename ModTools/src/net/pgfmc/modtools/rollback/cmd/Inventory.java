package net.pgfmc.modtools.rollback.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.rollback.inv.InventoryOnlinePlayersList;
import net.pgfmc.modtools.rollback.inv.inv.InventoryBackupList;

public class Inventory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("pgf.admin.inventory"))
		{
			p.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
			
			return true;
		}
		
		if (args.length == 0)
		{
			p.openInventory(new InventoryOnlinePlayersList().getInventory());
			
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null)
		{
			p.sendMessage(ChatColor.RED + "Could not find player " + args[0]);
			
			return true;
		}
		
		p.openInventory(new InventoryBackupList(PlayerData.from(target)).getInventory());
		
		return true;
		
	}

}
