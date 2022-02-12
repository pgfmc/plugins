package net.pgfmc.duel.general;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

/**
 * Duel Requester for managing duel requests.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 *
 */
public class DuelRequest extends RequestType {
	
	public static final DuelRequest DR = new DuelRequest();

	private DuelRequest() {
		super(120, "Duel");
		endsOnQuit = true;
		isPersistent = false;
	}

	@Override
	public ItemStack toItem() {
		return new ItemStack(Material.IRON_SWORD);
	}

	@Override
	protected boolean sendRequest(Request r) {
		return true;
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		
		switch(eB) {
		case DENIED:
			r.asker.sendMessage(r.target.getRankedName() + " §r§cHas rejected your challenge!");
			r.target.sendMessage("§cChallenge Rejected.");
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case ACCEPT:
			new Duel(r.asker, r.target);
			r.asker.sendMessage(r.target.getRankedName() + " §r§6has accepted your challenge!");
			r.target.sendMessage("§aYou have accepted the challenge!");
			break;
		case TIMEOUT:
			r.asker.sendMessage("§cThe challenge has timed out.");
			r.target.sendMessage("§cThe challenge has timed out.");
			break;
		case REFRESH:
			break;
		}
	}
}