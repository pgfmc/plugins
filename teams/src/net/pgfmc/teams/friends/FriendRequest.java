package net.pgfmc.teams.friends;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.teams.friends.Friends.Relation;

public class FriendRequest extends Request {

	public FriendRequest(PlayerData asker, PlayerData target) {
		super(asker, target, 120);
	}

	@Override
	protected void endRequest(EndBehavior arg0) {
		
		switch(arg0) {
		case DENIED:
			asker.sendMessage("§cYour friend request to " + target.getRankedName() + "§r§chas been rejected.");
			target.sendMessage("§cRequest Rejected.");
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case ACCEPT:
			Friends.setRelation(asker, Relation.FRIEND, target, Relation.FRIEND);
			asker.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			target.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			asker.sendMessage("§6Friend request sent to " + target.getRankedName());
			target.sendMessage(asker.getRankedName() + "§6has sent you a friend request!");
			target.sendMessage("§6Type §b/fa §6to accept!");
			break;
		case TIMEOUT:
			asker.sendMessage("§cFriend Request timed out.");
			target.sendMessage("§6Friend Request timed out.");
			break;
		}
	}

	@Override
	public ItemStack toItem() {
		return new ItemWrapper(Material.TOTEM_OF_UNDYING).gi();
	}
}
