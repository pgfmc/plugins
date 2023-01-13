package net.pgfmc.core.util.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import net.pgfmc.core.api.playerdata.PlayerData;

public abstract class PlayerPlayerArgumentCommand extends CommandBase {

	public PlayerPlayerArgumentCommand(String name) {
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
				.map(x -> x.getName())
				.collect(Collectors.toList());
	}
	
	public abstract boolean execute(CommandSender sender, String alias, PlayerData arg);
	public abstract boolean playerPredicate(PlayerData arg);
}
