package net.pgfmc.core.requests.cmd;

import java.util.List;
import java.util.Set;

import net.pgfmc.core.cmd.base.PlayerCmd;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

public class RequestDenyCommand extends PlayerCmd {
	
	private final RequestType rt;

	public RequestDenyCommand(String name, RequestType rt) {
		super(name);
		this.rt = rt;
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length > 0) { // if an argument was entered.
			
			PlayerData pds = PlayerData.getPlayerData(args[0]);
			if (pds != null) {
				Request r = rt.findRequest(pds, pd);
				if (r != null) {
					r.end(EndBehavior.DENIED);
					return true;
				}
			}
			
			pd.sendMessage("No request found for " + args[0]);
			return true;
		}
		
		Set<Request> set = rt.findRequests(pd);
		
		if (set.size() == 0) {
			pd.sendMessage("No requests to accept!");
			return true;
		} else if (set.size() == 1) {
			for (Request r : set) {
				r.end(EndBehavior.DENIED);
			}
			return true;
			
		} else {
			
			for (Request r : set) {
				pd.sendMessage(rt.name + " Request from " + r.asker + ".");
				
			}
			pd.sendMessage("You have " + set.size() + " Requests.");
		}
		return true;
	}
}
