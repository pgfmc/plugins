package net.pgfmc.shop.trade;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.inventoryAPI.extra.ItemWrapper;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

public class TradeRequester extends RequestType {
	
	public TradeRequester() {
		super(0, "Trade");
		this.endsOnQuit = false;
		this.isPersistent = true;
	}

	public final static TradeRequester TR = new TradeRequester();
	
	
	public static final void registerAll() {
		TR.registerDeny("tradedeny");
		TR.registerAccept("tradeaccept");
		TR.registerSend("traderequest");
	}

	@Override
	protected void requestMessage(Request r, boolean refreshed) {
		
		
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		
		switch(eB) {
		case ACCEPT:
			
			break;
		case DENIED:
			break;
		case FORCEEND:
			break;
		case QUIT:
			break;
		case REFRESH:
			break;
		case TIMEOUT:
			break;
		}
		
		
	}

	@Override
	protected ItemStack toItem() {
		return new ItemWrapper(Material.MAGENTA_GLAZED_TERRACOTTA).n("Trade").gi();
	}
}
