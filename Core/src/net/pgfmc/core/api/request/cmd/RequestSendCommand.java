package net.pgfmc.core.api.request.cmd;

import java.util.ArrayList;
import java.util.List;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.RequestType;
import net.pgfmc.core.util.commands.PlayerCommand;

public class RequestSendCommand extends PlayerCommand {
	
	private final RequestType rt;
	
	public RequestSendCommand(String name, RequestType rt) {
		super(name);
		this.rt = rt;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length < 1) {
			pd.sendMessage("§cPlease enter a player!");
			return true;
		}
		
		@SuppressWarnings("deprecation")
		PlayerData target = PlayerData.from(args[0]);
		if (target == null) {
			pd.sendMessage("§cPlayer not found");
			return true;
			
		} else if (!target.isOnline() && rt.endsOnQuit()) {
			pd.sendMessage("§cPlayer is not online to receive reqeust.");
			return true;
			
		} else if (pd == target) {
			pd.sendMessage("§cYou can't send a request to yourself!");
			return true;
			
		}
		rt.createRequest(pd, target);
		
		return true;
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		
		List<String> list = new ArrayList<>();
		
		if (args.length == 0 || args.length == 1) {
			if (rt.endsOnQuit()) {
				for (PlayerData pds : PlayerData.getPlayerDataSet(x -> x.isOnline())) {
					if (pds == pd) continue;
					
					if (pds.getName().startsWith(args[0])) {
						list.add(pds.getName());
					}
				}
			} else {
				for (PlayerData pds : PlayerData.getPlayerDataSet()) {
					if (pds == pd) continue;
					if (pds.getName().startsWith(args[0])) {
						list.add(pds.getName());
					}
				}
			}
		}
		
		return list;
	}
}
