package net.pgfmc.teams.friends;

import org.bukkit.Sound;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends.Relation;

/**
 * Command to unfriend a Friend. lots a cheques lool
 * 
 * @author CrimsonDart
 * @since 1.2.0
 * @version 4.0.3
 */
public class UnfriendCommand extends FriendCommandBase {

	@Override
	public boolean action(PlayerData player, PlayerData friend) {
		switch(Friends.getRelation(player, friend)) {
		case FAVORITE:
			Friends.setRelation(player, Relation.NONE, friend, Relation.NONE);
			player.sendMessage("§cUnfriended §n" + friend.getRankedName() + "§r§c.");
			player.playSound(Sound.BLOCK_CALCITE_HIT);
			break;
			
		case FRIEND:
			Friends.setRelation(player, Relation.NONE, friend, Relation.NONE);
			player.sendMessage("§cUnfriended §n" + friend.getRankedName() + "§r§c.");
			player.playSound(Sound.BLOCK_CALCITE_HIT);
			break;
			
		case NONE:
			player.sendMessage("§n" + friend.getRankedName() + "§r§c isn't your friend.");
			break;
			
		case SELF:
			player.sendMessage("§6 You can't unfriend yourself!");
			break;
		}
		
		return true;
	}
}