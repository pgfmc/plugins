package net.pgfmc.shop.trade;

import net.pgfmc.core.requestAPI.Requester;

public class TradeRequester extends Requester {

	public TradeRequester() {
		super("Trade", 300, (p1, p2) -> {
			
			new TradeManager(p1, p2);
			return true;
			
		});
	}
}
