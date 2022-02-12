package net.pgfmc.friends.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public abstract class FriendCommandBase implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cOnly players can use this command!");
			return true;
		}
			
		if (args == null || args[0].isBlank()) {
			return false;
		}
		
		PlayerData friend = PlayerData.from(args[0]);
		
		if (friend == null) {
			sender.sendMessage("§cCouldn't find player " + args[0] + ".");
			return true;
		}
		
		PlayerData player = PlayerData.from((Player) sender);
		
		return action(player, friend);
	}
	
	public abstract boolean action(PlayerData player, PlayerData friend);
	
}
