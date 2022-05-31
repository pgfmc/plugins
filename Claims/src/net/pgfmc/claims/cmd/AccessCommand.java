package net.pgfmc.claims.cmd;

import java.util.List;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;

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
