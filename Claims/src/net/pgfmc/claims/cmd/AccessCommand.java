package net.pgfmc.claims.cmd;

import java.util.List;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class AccessCommand extends PlayerCommand {

	public AccessCommand(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(PlayerData arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> tabComplete(PlayerData arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
