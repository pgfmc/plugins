package net.pgfmc.ffa.cmd.zone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryType;

import net.pgfmc.ffa.cmd.handler.FFACmd;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Clear implements FFACmd {

	@Override
	public void CmdDo(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "Please use `/ffa zones` for a list of zones.");
			return;
		}
		
		Zone zone = Zone.valueOf(args[0].toUpperCase());
		
		if (zone == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid zone name: Please use `/ffa zones` for a list of zones.");
			return;
		}
		
		zone.setInventoryItems(Bukkit.createInventory(null, InventoryType.PLAYER));
		
		sender.sendMessage(ChatColor.GREEN + "Successfully cleared zone inventory!");
		
	}

}
