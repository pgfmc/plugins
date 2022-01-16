package net.pgfmc.teams.friends;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for Accepting a friend Request.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
public class FriendAcceptCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Data output to the console.");
		}
		
		Friends.DEFAULT.accept((Player) sender);
		
		return true;
	}
}
