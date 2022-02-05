package net.pgfmc.core.requests.cmd;

import java.util.ArrayList;
import java.util.List;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.RequestType;

public class RequestSendCommand extends PlayerCommand {
	
	private final RequestType rt;
	
	public RequestSendCommand(String name, RequestType rt) {
		super(name);
		this.rt = rt;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length < 1) {
			pd.sendMessage("Please enter a player!");
			return true;
		}
		
		PlayerData target = PlayerData.getPlayerData(args[0]);
		if (target == null) {
			pd.sendMessage("Player not found");
			return true;
			
		} else if (!target.isOnline() && rt.endsOnQuit()) {
			pd.sendMessage("Player is not online to receive reqeust.");
			return true;
			
		} else if (pd == target) {
			pd.sendMessage("You can't send a request to yourself!");
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
					
					if (pds.getNicknameRaw().startsWith(args[0])) {
						list.add(pds.getNicknameRaw());
					}
				}
			} else {
				for (PlayerData pds : PlayerData.getPlayerDataSet()) {
					if (pds == pd) continue;
					if (pds.getNicknameRaw().startsWith(args[0])) {
						list.add(pds.getNicknameRaw());
					}
				}
			}
		}
		
		return list;
	}
}
