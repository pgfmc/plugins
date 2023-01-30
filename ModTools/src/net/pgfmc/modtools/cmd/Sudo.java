package net.pgfmc.modtools.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Forcefully force a player to execute a command or message
 * @author bk
 *
 */
public class Sudo implements CommandExecutor {
	
	/**
	 * Must include a valid player and message or /command, (include the slash for commands)
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length < 2) return false;
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null)
		{
			sender.sendMessage(ChatColor.RED + "Could not find player " + ChatColor.GOLD + ChatColor.UNDERLINE + args[0] + ChatColor.RESET + ChatColor.RED + ".");
			return true;
		}
		
		String msg = String.join(" ", args).replace(args[0], "");
		
		target.chat(msg);
		sender.sendMessage(ChatColor.GREEN + "Command successfully ran!");
		
		return true;
	}
	
}
