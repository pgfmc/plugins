package net.pgfmc.ffa.cmd.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import net.pgfmc.ffa.cmd.handler.FFACmd;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Preview implements FFACmd {

	@Override
	public void CmdDo(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return;
		}
		
		Player p = (Player) sender;
		
		Zone zone = Zone.valueOf(args[2].toUpperCase());
		
		if (zone == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid zone name: Please use `/ffa zones` for a list of zones.");
			return;
		}
		Inventory zoneInventory = Bukkit.createInventory(null, InventoryType.PLAYER);
		zoneInventory.setContents(zone.getContents());
		
		p.openInventory(zoneInventory);
		
		sender.sendMessage(ChatColor.GREEN + "Successfully previewed zone inventory!");
		
	}

}
