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
	}

	public final static TradeRequester DEFAULT = new TradeRequester();

	@Override
	protected boolean sendRequest(Request r) {
		return true;
		
		
		
		
		
	}

	@Override
	protected void endRequest(Request r, EndBehavior eB) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ItemStack toItem() {
		return new ItemWrapper(Material.MAGENTA_GLAZED_TERRACOTTA).n("Trade").gi();
	}
}
