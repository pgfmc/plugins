package net.pgfmc.shop.trade;

import org.bukkit.entity.Player;

import net.pgfmc.core.requestAPI.Request;
import net.pgfmc.core.requestAPI.Requester;

public class TradeRequester extends Requester {
	
	public final static TradeRequester DEFAULT = new TradeRequester();

	private TradeRequester() {
		super("Trade", 300, (p1, p2) -> {
			
			new TradeManager(p1, p2);
			return true;
			
		});
	}
	
	@Override
	public Request createRequest(Player init, Player target) {
		
		
		
		
		
		return null;
	}
}
