package net.pgfmc.ffa.cmd.handler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.pgfmc.ffa.cmd.inventory.Clear;
import net.pgfmc.ffa.cmd.inventory.Set;
import net.pgfmc.ffa.cmd.zones.List;

public class FFACmdHandler implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + "Commands for PGF-FFA" + ChatColor.RESET
					+ "\n" + ChatColor.RED + "/ffa inventory <set/clear/preview> [zone]"
					+ "\n" + ChatColor.RED + "/ffa zones");
			return true;
		}
		
		if (args.length == 1)
		{
			if (args[0].equals("zones"))
			{
				new List().CmdDo(sender, cmd, label, args);
				return true;
			}
			
		}
		
		if (args.length == 3)
		{
			if (args[0].equals("inventory"))
			{
				if (args[1].equals("set"))
				{
					new Set().CmdDo(sender, cmd, label, args);
					return true;
				}
				
				if (args[1].equals("clear"))
				{
					new Clear().CmdDo(sender, cmd, label, args);
					return true;
				}
				
				if (args[1].equals("preview"))
				{
					new Clear().CmdDo(sender, cmd, label, args);
					return true;
				}
				
			}
			
		}
		
		
		return false;
	}
	
	

}
