package net.pgfmc.friends.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.friends.data.Friends;

/**
 * Sends a message of all the Player's friends to them.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
public class FriendsListCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return true;
		}
		
		for (PlayerData pd : Friends.getFriendsMap(PlayerData.from((Player) sender)).keySet()) {
			sender.sendMessage("�n" + pd.getRankedName());
		}
		
		return true;
	}

}
