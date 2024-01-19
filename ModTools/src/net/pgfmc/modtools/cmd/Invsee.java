package net.pgfmc.modtools.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Invsee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		// GUI
		if (args.length == 0)
		{
			// TODO
			return false;
		} else { // NO GUI
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target == null)
			{
				p.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
				return true;
			}
			
			if (args.length == 1)
			{
				p.sendMessage(ChatColor.GREEN + "Opening inventory of player " + args[0] + ".");
				p.openInventory(target.getInventory());
			} else if (args.length == 2 && args[1].equals("echest"))
			{
				p.sendMessage(ChatColor.GREEN + "Opening enderchest of player " + args[0] + ".");
				p.openInventory(target.getEnderChest());
			}
			
			return true;
			
		}
		
		
	}

}
