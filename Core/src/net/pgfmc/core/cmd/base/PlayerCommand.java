package net.pgfmc.core.cmd.base;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
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
			sender.sendMessage("§cThe sender must be a player!");
			return true;
		}
		
		PlayerData pd = PlayerData.from((Player) sender);
		if (pd == null) {
			sender.sendMessage("§cThe sender must be a player!");
			return true;
		}
		
		if (this instanceof CreativeOnly && pd.getPlayer().getGameMode() != GameMode.CREATIVE) {
			sender.sendMessage("§cYou must be in creative mode to use this command!");
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
