package net.pgfmc.core.util.commands;

import java.util.List;
import java.util.stream.Collectors;

import net.pgfmc.core.api.playerdata.PlayerData;

public abstract class ServerPlayerAtPlayerCommand extends PlayerCommand {

	public ServerPlayerAtPlayerCommand(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData sender, String alias, String[] args) {
		
		return PlayerData.getPlayerDataSet(x -> playerPredicate(x, sender)).stream()
				.map(x -> x.getDisplayNameRaw())
				.collect(Collectors.toList());
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length < 1) {
			pd.sendMessage("§cPlease input a player's name!");
			return true;
		}
		
		PlayerData pdarg = PlayerData.from(args[0]);
		
		if (pdarg == null) {
			pd.sendMessage("§cPlease input a player's name!");
			return true;
		}
		
		return execute(pd, alias, pdarg);
	}
	
	
	public abstract boolean execute(PlayerData pd, String alias, PlayerData arg);
	public abstract boolean playerPredicate(PlayerData arg, PlayerData sender);

}
