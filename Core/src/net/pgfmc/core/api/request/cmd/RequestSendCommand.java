package net.pgfmc.core.api.request.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
			pd.sendMessage(ChatColor.RED + "Please enter a player!");
			return true;
		}
		
		final Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null) {
			pd.sendMessage(ChatColor.RED + "Player not found");
			return true;
			
		} else if (!player.isOnline() && rt.endsOnQuit()) {
			pd.sendMessage(ChatColor.RED + "Player is not online to receive reqeust.");
			return true;
			
		} else if (pd.getPlayer() == player) {
			pd.sendMessage(ChatColor.RED + "You can't send a request to yourself!");
			return true;
			
		}
		PlayerData target = PlayerData.from(player);
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
