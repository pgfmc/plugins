package net.pgfmc.survival.cmd.pvp;

import java.util.List;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class Pvp  extends PlayerCommand {

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
			pd.sendMessage("§cPvP Disabled!");
			
		} else {
			pd.addTag("pvp");
			pd.sendMessage("§aPvP Enabled!");
		}
		
		
		return true;
	}
}
