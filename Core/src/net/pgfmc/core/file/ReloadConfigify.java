package net.pgfmc.core.file;

import org.bukkit.ChatColor;
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
			sender.sendMessage(ChatColor.GREEN + "Configify reloaded!");
			Configify.reloadConfigify();
		}
		
		return true;
	}
	
	public void reload()
	{
		Configify.reloadConfigify();
	}
}
