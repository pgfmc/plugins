package net.pgfmc.core.cmd.base;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public abstract class PlayerCmd extends CmdBase {

	public PlayerCmd(String name) {
		super(name);
		System.out.print("2..");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("The sender must be a player!");
			return true;
		}
		
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		if (pd == null) {
			sender.sendMessage("The sender must be a player!");
			return true;
		}
		
		return execute(pd, alias, args);
	}
	
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("The sender must be a player!");
			return null;
		}
		
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		if (pd == null) {
			sender.sendMessage("The sender must be a player!");
			return null;
		}
		
		return tabComplete(pd, alias, args);
	}
	
	public abstract List<String> tabComplete(PlayerData pd, String alias, String[] args);
	public abstract boolean execute(PlayerData pd, String alias, String[] args);
}
