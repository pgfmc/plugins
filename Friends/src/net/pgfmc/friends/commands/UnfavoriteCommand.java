package net.pgfmc.friends.commands;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.friends.data.Friends;
import net.pgfmc.friends.data.Friends.Relation;

/**
 * Command to Unfavorite a favorited player.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 * @version 4.0.3
 */
public class UnfavoriteCommand extends FriendCommandBase {

	@Override
	public boolean action(PlayerData player, PlayerData friend) {
		switch(Friends.getRelation(player, friend)) {
		case FAVORITE:
			Friends.setRelation(player, friend, Relation.FRIEND);
			player.sendMessage("§6Unfavorited §n" + friend.getRankedName() + "§r§c.");
			break;
		case FRIEND:
			player.sendMessage("§n" + friend.getRankedName() + "§r§6 is not favorited.");
			break;
		case NONE:
			player.sendMessage("§n" + friend.getRankedName() + "§r§c isn't your friend.");
			break;
		case SELF:
			player.sendMessage("§6You can't unfavorite yourself!");
			break;
		}
		
		return true;
	}
}
