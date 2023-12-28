package net.pgfmc.survival.cmd.pvp;

import java.util.List;

import org.bukkit.ChatColor;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.core.util.roles.PGFRoles;

public class Pvp extends PlayerCommand {

	public Pvp() {
		super("pvp");
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (pd.hasTag("pvp")) {
			pd.removeTag("pvp");
			pd.sendMessage(ChatColor.RED + "PvP Disabled!");
			
			PGFRoles.updatePlayerNameplate(pd);
			
		} else {
			pd.addTag("pvp");
			pd.sendMessage(ChatColor.GREEN + "PvP Enabled!");
			
			PGFRoles.updatePlayerNameplate(pd);
		}
		
		
		return true;
	}
	
}
