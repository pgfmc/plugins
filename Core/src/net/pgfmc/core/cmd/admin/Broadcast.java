package net.pgfmc.core.cmd.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Broadcast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0)
		{
			return false;
		}
		
		String msg = String.join(" ", args).replace("&", "§");
		Bukkit.broadcastMessage(msg);
		
		return true;
	}

}
