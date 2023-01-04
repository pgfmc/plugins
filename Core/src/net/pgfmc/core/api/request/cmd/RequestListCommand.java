package net.pgfmc.core.api.request.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.api.request.inv.RequestListInventory;
import net.pgfmc.core.util.commands.PlayerCommand;

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
