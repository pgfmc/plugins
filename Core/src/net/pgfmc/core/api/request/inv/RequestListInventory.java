package net.pgfmc.core.api.request.inv;

import java.util.LinkedList;
import java.util.List;

import net.pgfmc.core.api.inventory.ButtonInventory;
import net.pgfmc.core.api.inventory.extra.Buttonable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;

public class RequestListInventory extends ButtonInventory {
	
	
	private PlayerData pd;
	
	public RequestListInventory(PlayerData pd) {
		super(27, "Requests");
		this.pd = pd;
		if (pd != null) {
			System.out.println("pd isn't null");
		}
		refresh();
	}
	
	@Override
	protected List<Buttonable> load() {
		
		
		if (pd == null) {
			System.out.println("pd is null");
		}
		
		List<Buttonable> list = new LinkedList<>();
		
		for (Request r : RequestType.getInAllRequests(x -> x.target == pd)) {
			System.out.println("Request added");
			list.add(r);
		}
		
		return list;
	}
}
