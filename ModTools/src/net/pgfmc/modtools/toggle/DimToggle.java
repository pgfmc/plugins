package net.pgfmc.modtools.toggle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class DimToggle implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length != 1) { return false; }
		if (Bukkit.getWorld(args[0]) == null) { return false; }
		
		// TODO
		
		return true;
	}
	
	

}
