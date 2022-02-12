package net.pgfmc.friends.data;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;
import net.pgfmc.friends.data.Friends.Relation;

public class FriendRequest extends RequestType {

	public static final FriendRequest FR = new FriendRequest();

	public FriendRequest() {
		super(0, "Friend");
		endsOnQuit = false;
		isPersistent = true;
		setJoinMessage("§6You have a pending friend Request!");
	}
	
	public static final void registerAll() {
		FR.registerDeny("friendDeny");
		FR.registerAccept("friendAccept");
		FR.registerSend("friendRequest");
	}
	
	@Override
	public ItemStack toItem() {
		return new ItemWrapper(Material.TOTEM_OF_UNDYING).gi();
	}

	@Override
	protected boolean sendRequest(Request r) {
		if (Friends.getRelation(r.asker, r.target).isFriend()) {
			r.asker.sendMessage("§cThey are already your friend!");
			return false;
		}
		
		r.asker.sendMessage("§6Friend Request sent to " + r.target.getRankedName() + "!");
		r.target.sendMessage(r.asker.getRankedName() + " §6has sent you a friend request!");
		r.target.sendMessage("§6Use /fa to accept!");
		return true;
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		switch(eB) {
		case DENIED:
			r.asker.sendMessage("§cYour friend request to " + r.target.getRankedName() + "§r§chas been rejected.");
			r.target.sendMessage("§cRequest Rejected.");
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case ACCEPT:
			Friends.setRelation(r.asker, Relation.FRIEND, r.target, Relation.FRIEND);
			r.asker.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			r.target.playSound(Sound.BLOCK_AMETHYST_BLOCK_HIT);
			r.asker.sendMessage(r.target.getRankedName() + " §ahas accepted your friend request!");
			r.target.sendMessage("§aYou have accepted " + r.asker.getRankedName() + "'s §afriend request!");
			break;
		case TIMEOUT:
			r.asker.sendMessage("§cFriend Request timed out.");
			r.target.sendMessage("§6Friend Request timed out.");
			break;
		case REFRESH:
			r.asker.sendMessage("§6Your request has already been sent!");
			break;
		}
	}
}
