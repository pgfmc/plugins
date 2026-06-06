package net.pgfmc.survival.gift;

import java.util.List;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class GiftCommand extends PlayerCommand {


    public GiftCommand(String name) {
        super(name);
        
    }

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

    @Override
    public boolean execute(PlayerData pd, String alias, String[] args) {
        
        return true;
    }

}
