package net.pgfmc.teams.friends;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requestAPI.Request;

/**
 * Command for sending a Friend Request.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 */
public class FriendRequestCommand extends FriendCommandBase {

	@Override
	public boolean action(PlayerData player, PlayerData friend) {
		Request r = Friends.DEFAULT.findRequest(player.getUniqueId(), friend.getUniqueId());
		if (r != null) {
			Friends.DEFAULT.accept(r);
			return true;
		}
		
		switch(Friends.getRelation(player, friend)) {
		case FAVORITE:
			player.sendMessage("§n" + friend.getRankedName() + "§r§6 is already your friend!");
			break;
			
		case FRIEND:
			player.sendMessage("§n" + friend.getRankedName() + "§r§6 is already your friend!");
			break;
			
		case NONE:
			Friends.DEFAULT.createRequest(player.getPlayer(), friend.getPlayer()).setMessage(Friends.RM);
			player.sendMessage("§aFriend Request send to §n" + friend.getRankedName() + "§r§a.");
			break;
			
		case SELF:
			player.sendMessage("§r§6You can't friend yourself!");
			break;
		}
		
		return true;
	}
}