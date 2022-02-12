package net.pgfmc.core.requests.inv;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import net.pgfmc.core.inventoryAPI.ButtonInventory;
import net.pgfmc.core.inventoryAPI.extra.Buttonable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

public class RequestListInventory extends ButtonInventory {
	
	
	private final PlayerData pd;
	
	
	public RequestListInventory(PlayerData pd) {
		super(27, "Requests");
		this.pd = pd;
	}
	
	@Override
	protected List<Buttonable> load() {
		
		Objects.requireNonNull(pd);
		
		List<Buttonable> list = new LinkedList<>();
		
		for (Request r : RequestType.getInAllRequests(x -> {
			return x.target.equals(pd);
		})) {
			
			list.add(r);
		}
		
		return list;
	}
}
