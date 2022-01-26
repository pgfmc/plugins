package net.pgfmc.teams.duel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;

/**
 * Duel Requester for managing duel requests.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 *
 */
public class DuelRequest extends Request {

	public DuelRequest(PlayerData asker, PlayerData target) {
		super(asker, target, 30);
	}

	@Override
	protected void endRequest(EndBehavior arg0) {
		
		
		switch(arg0) {
		case DENIED:
			asker.sendMessage(target.getRankedName() + " �r�cHas rejected your challenge!");
			target.sendMessage("�cChallenge Rejected.");
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case ACCEPT:
			new Duel(asker, target);
			asker.sendMessage(target.getRankedName() + " �r�6has accepted your challenge!");
			target.sendMessage("�aYou have accepted the challenge!");
			break;
		case TIMEOUT:
			asker.sendMessage("�cThe challenge has timed out.");
			target.sendMessage("�cThe challenge has timed out.");
			break;
		}
	}

	@Override
	public ItemStack toItem() {
		return new ItemStack(Material.IRON_SWORD);
	}
}