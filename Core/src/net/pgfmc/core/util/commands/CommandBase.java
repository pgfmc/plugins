package net.pgfmc.core.util.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public abstract class CommandBase {
	
	public CommandBase(String name) {
		Bukkit.getPluginCommand(name).setExecutor(executor);
		Bukkit.getPluginCommand(name).setTabCompleter(executor);
	}
	
	private TabExecutor executor = new TabExecutor() {
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
			return execute(sender, alias, args);
		}

		@Override
		public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
			
			List<String> list = tabComplete(arg0, arg2, arg3);
			
			if (list == null) {
				return new ArrayList<>();
			} else {
				return list;
			}
		}
	};
	
	public abstract boolean execute(CommandSender sender, String alias, String[] args);
	public abstract List<String> tabComplete(CommandSender sender, String alias, String[] args);
}