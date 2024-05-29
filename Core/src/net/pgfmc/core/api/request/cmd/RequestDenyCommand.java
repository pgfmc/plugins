package net.pgfmc.core.api.request.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.EndBehavior;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.util.commands.PlayerCommand;

public class RequestDenyCommand extends PlayerCommand {
	
	private final RequestType rt;

	public RequestDenyCommand(String name, RequestType rt) {
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
				list.add(r.asker.getName());
			}
		}
		
		return list;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length > 0) { // if an argument was entered.
			final Player player = Bukkit.getPlayer(args[0]);
			
			if (player == null)
			{
				pd.sendMessage(ChatColor.RED + "No request found for " + args[0]);
				return true;
			}
			
			PlayerData pds = PlayerData.from(player);
			if (pds != null) {
				Request r = rt.findRequest(pds, pd);
				if (r != null) {
					r.end(EndBehavior.DENIED);
					return true;
				}
			}
			
			pd.sendMessage(ChatColor.RED + "No request found for " + args[0]);
			return true;
		}
		
		Set<Request> set = rt.findRequests(pd);
		
		if (set.size() == 0) {
			pd.sendMessage(ChatColor.RED + "No requests to Deny!");
			return true;
		} else if (set.size() == 1) {
			for (Request r : set) {
				r.end(EndBehavior.DENIED);
			}
			return true;
			
		} else {
			
			for (Request r : set) {
				pd.sendMessage(ChatColor.GOLD + rt.name + " Request from " + r.asker + ".");
				
			}
			pd.sendMessage(ChatColor.GOLD + "You have " + set.size() + " Requests.");
		}
		return true;
	}
}
