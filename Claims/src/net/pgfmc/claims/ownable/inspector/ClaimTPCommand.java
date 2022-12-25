package net.pgfmc.claims.ownable.inspector;

import java.util.List;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

public class ClaimTPCommand extends PlayerCommand {
	
	public ClaimTPCommand(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		Claim ob = ClaimsTable.getClosestClaim(new Vector4(pd.getPlayer().getLocation()), Range.PROTECTED);
		
		if (ob != null) {
			pd.teleport(ob.getLocation().toLocation());
			pd.sendMessage("§aTeleported to the active claim!");
		} else {
			pd.sendMessage("§6No claim in range.");
		}
		return false;
	}
}
