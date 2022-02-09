package net.pgfmc.core.requests.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;
import net.pgfmc.core.requests.inv.RequestListInventory;

public class RequestListCommand extends PlayerCommand {

	public RequestListCommand() {
		super("requestList");
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return new ArrayList<>();
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		Set<Request> rs = RequestType.getInAllRequests(x -> {
			return (x.target == pd);
		});
		
		for (Request r : rs) {
			pd.sendMessage(r.getType() + " Request from " + r.asker.getRankedName());
		}
		
		new RequestListInventory(pd);
		
		return false;
	}
}
