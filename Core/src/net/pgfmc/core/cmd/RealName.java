package net.pgfmc.core.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.cmd.base.PlayerArg1Cmd;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class RealName extends PlayerArg1Cmd {

	public RealName(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, PlayerData arg) {
		sender.sendMessage(ChatColor.GOLD + "Real name for " + arg.getRankedName() + ChatColor.GOLD + " is " + arg.getRankColor() + arg.getName() + ChatColor.GOLD + "!");
		return true;
	}

	@Override
	public boolean playerPredicate(PlayerData arg) {
		return true;
	}
}
