package net.pgfmc.core.api.request.inv;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;

import net.pgfmc.core.api.inventory.ButtonInventory;
import net.pgfmc.core.api.inventory.extra.Buttonable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;

public class RequestListInventory extends ButtonInventory {
	
	
	private PlayerData pd;
	
	public RequestListInventory(PlayerData pd) {
		super(27, ChatColor.RESET + "" + "Requests Menu");
		this.pd = pd;
		
	}
	
	@Override
	protected List<Buttonable> load() {
		
		List<Buttonable> matchedRequestsList = new LinkedList<>();
		
		for (Request matchedRequest : RequestType.getInAllRequests(request -> request.target == pd)) {
			matchedRequestsList.add(matchedRequest);
		}
		
		return matchedRequestsList;
	}
	
}
