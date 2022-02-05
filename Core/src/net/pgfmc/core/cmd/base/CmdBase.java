package net.pgfmc.core.cmd.base;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public abstract class CmdBase {
	
	public CmdBase(String name) {
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
			return tabComplete(arg0, arg2, arg3);
		}
	};
	
	public abstract boolean execute(CommandSender sender, String alias, String[] args);
	public abstract List<String> tabComplete(CommandSender sender, String alias, String[] args);
}