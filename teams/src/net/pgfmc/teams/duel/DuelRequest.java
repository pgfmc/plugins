package net.pgfmc.teams.duel;

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
		super(asker, target, 30, true);
	}

	@Override
	protected void endRequest(EndBehavior arg0) {
		
		
		switch(arg0) {
		case DENIED:
			asker.sendMessage(target.getRankedName() + " §r§cHas rejected your challenge!");
			target.sendMessage("§cChallenge Rejected.");
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case SUCCESSFUL:
			new Duel(asker, target);
			asker.sendMessage(target.getRankedName() + " §r§6has accepted your challenge!");
			target.sendMessage("§aYou have accepted the challenge!");
			break;
		case TIMEOUT:
			asker.sendMessage("§cThe challenge has timed out.");
			target.sendMessage("§cThe challenge has timed out.");
			break;
		}
	}
}