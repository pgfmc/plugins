package net.pgfmc.core.util.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

public abstract class ServerPlayerAtPlayerCommand extends PlayerCommand {

	public ServerPlayerAtPlayerCommand(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData sender, String alias, String[] args) {
		
		return PlayerData.getPlayerDataSet(x -> playerPredicate(x, sender)).stream()
				.map(x -> x.getName())
				.collect(Collectors.toList());
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length < 1) {
			pd.sendMessage(ChatColor.RED + "Please input a player's name!");
			return true;
		}
		
		final Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null) {
			pd.sendMessage(ChatColor.RED + "Please input a player's name!");
			return true;
		}
		
		PlayerData pdarg = PlayerData.from(player);
		
		return execute(pd, alias, pdarg);
	}
	
	
	public abstract boolean execute(PlayerData pd, String alias, PlayerData arg);
	public abstract boolean playerPredicate(PlayerData arg, PlayerData sender);

}
