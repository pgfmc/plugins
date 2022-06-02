package net.pgfmc.ffa.cmd.inventory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.ffa.cmd.handler.FFACmd;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Set implements FFACmd {

	@Override
	public void CmdDo(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return;
		}
		
		Zone zone = Zone.valueOf(args[2].toUpperCase());
		
		if (zone == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid zone name: Please use `/ffa zones` for a list of zones.");
			return;
		}
		
		zone.setContents(((Player) sender).getInventory().getContents());
		
		sender.sendMessage(ChatColor.GREEN + "Successfully set zone inventory!");
		
	}

}
