package net.pgfmc.ffa.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class SetZoneInventory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		if (args.length == 0) return false;
		
		Zone zone = Zone.valueOf(args[0]);
		
		if (zone == null) return false;
		
		zone.setInventoryItems(((Player) sender).getInventory());
		
		sender.sendMessage(ChatColor.GREEN + "Successfully set zone inventory!");
		
		return true;
	}

}
