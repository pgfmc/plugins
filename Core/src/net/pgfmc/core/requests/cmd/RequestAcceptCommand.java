package net.pgfmc.core.requests.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

public class RequestAcceptCommand extends PlayerCommand {
	
	public final RequestType rt;

	public RequestAcceptCommand(String name, RequestType rt) {
		super(name);
		this.rt = rt;
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		
		Set<Request> set = rt.findRequests(pd);
		List<String> list = new ArrayList<>();
		
		if (!(args.length == 0 || args.length == 1)) return list;
		
		if (set.size() == 0) {
			return list;
		} else  {
			for (Request r : set) {
				list.add(r.asker.getDisplayNameRaw());
			}
		}
		
		return list;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		if (args.length > 0) { // if an argument was entered.
			
			PlayerData pds = PlayerData.from(args[0]);
			if (pds != null) {
				Request r = rt.findRequest(pds, pd);
				if (r != null) {
					r.end(EndBehavior.ACCEPT);
					return true;
				}
			}
			
			pd.sendMessage("§cNo request found for " + args[0]);
			return true;
		}
		
		Set<Request> set = rt.findRequests(pd);
		
		if (set.size() == 0) {
			pd.sendMessage("§cNo requests to accept!");
			return true;
		} else if (set.size() == 1) {
			for (Request r : set) {
				r.end(EndBehavior.ACCEPT);
			}
			return true;
			
		} else {
			for (Request r : set) {
				pd.sendMessage("§c" + rt.name + " Request from " + r.asker + ".");
			}
			pd.sendMessage("§6Use §b/" + alias + " <sender name> §6to pick a Request.");
		}
		return true;
	}

}
