package net.pgfmc.core.requests.inv;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;

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
		
		List<Buttonable> list = new LinkedList<>();
		
		for (Request r : RequestType.getInAllRequests(x -> x.target.equals(pd) && !x.isEnded)) {
			
			list.add(r);
			Bukkit.getLogger().warning("Found request belonging to player " + r.target.getRankedName());
		}
		
		return list;
	}
}
