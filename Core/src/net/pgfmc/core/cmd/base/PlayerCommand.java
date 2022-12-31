package net.pgfmc.core.cmd.base;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public abstract class PlayerCommand extends CmdBase {

	public PlayerCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "The sender must be a player!");
			return true;
		}
		
		PlayerData pd = PlayerData.from((Player) sender);
		if (pd == null) {
			sender.sendMessage(ChatColor.RED + "The sender must be a player!");
			return true;
		}
		
		
		return execute(pd, alias, args);
	}
	
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		
		if (!(sender instanceof Player)) {
			return new ArrayList<>();
		}
		
		PlayerData pd = PlayerData.from((Player) sender);
		if (pd == null) {
			return new ArrayList<>();
		}
		
		return tabComplete(pd, alias, args);
	}
	
	public abstract List<String> tabComplete(PlayerData pd, String alias, String[] args);
	public abstract boolean execute(PlayerData pd, String alias, String[] args);
}
