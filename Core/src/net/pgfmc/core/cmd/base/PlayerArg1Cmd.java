package net.pgfmc.core.cmd.base;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import net.pgfmc.core.playerdataAPI.PlayerData;

public abstract class PlayerArg1Cmd extends CmdBase {

	public PlayerArg1Cmd(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		PlayerData pdarg = CommandUtils.parsePlayerName(args[0], sender);
		if (pdarg == null) return true;
		
		return execute(sender, alias, pdarg);
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		
		if (args.length != 1) return null;
		
		return PlayerData.getPlayerDataSet(x -> playerPredicate(x)).stream()
				.map(x -> x.getDisplayNameRaw())
				.collect(Collectors.toList());
	}
	
	public abstract boolean execute(CommandSender sender, String alias, PlayerData arg);
	public abstract boolean playerPredicate(PlayerData arg);
}
