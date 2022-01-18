package net.pgfmc.core.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigify implements CommandExecutor {
	
	/**
	 * Command to reload the configs
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 1 && args[0].equals("reload"))
		{
			Configify.reloadConfigs();
		}
		
		return true;
	}
	
	public void init()
	{
		Configify.reloadConfigs();
	}
}
