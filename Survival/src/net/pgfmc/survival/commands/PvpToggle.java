package net.pgfmc.survival.commands;

import java.util.List;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class PvpToggle  extends PlayerCommand {

	public PvpToggle() {
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
			pd.sendMessage("§cPvP Disabled!");
			
		} else {
			pd.addTag("pvp");
			pd.sendMessage("§aPvP Enabled!");
		}
		
		
		return true;
	}
}
