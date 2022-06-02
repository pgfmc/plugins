package net.pgfmc.ffa.cmd.inventory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		
		p.openInventory(zone.getInventory());
		
		sender.sendMessage(ChatColor.GREEN + "Successfully previewed zone inventory!");
		
	}

}
