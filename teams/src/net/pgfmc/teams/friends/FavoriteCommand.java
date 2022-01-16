package net.pgfmc.teams.friends;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;
/**
 * Command for Favoriting a Friend.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 * @version 4.0.3
 */
public class FavoriteCommand extends FriendCommandBase {

	@Override
	public boolean action(PlayerData player, PlayerData friend) {
		switch(Friends.getRelation(player, friend)) {
		case FAVORITE:
			player.sendMessage("§n" + friend.getRankedName() + "§r§6 is already favorited!");
			break;
			
		case FRIEND:
			Friends.setRelation(player, friend, Relation.FAVORITE);
			player.sendMessage("§aFavorited §n" + friend.getRankedName() + "§r§c.");
			break;
			
		case NONE:
			player.sendMessage("§n" + friend.getRankedName() + "§r§c isn't your friend.");
			break;
			
		case SELF:
			player.sendMessage("§6You can't favorite yourself!");
			break;
		}
		
		return true;
	}
}