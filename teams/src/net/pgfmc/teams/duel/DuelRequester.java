package net.pgfmc.teams.duel;

import net.pgfmc.core.requestAPI.Requester;

/**
 * Duel Requester for managing duel requests.
 * 
 * @author CrimsonDart
 * @since 1.2.0	
 *
 */
public class DuelRequester extends Requester {
	
	public static final DuelRequester DEFAULT = new DuelRequester();

	private DuelRequester() {
		super("Duel", 30, (x, y) -> {
			
			new Duel(x, y);
			return true;
		});
	}
}