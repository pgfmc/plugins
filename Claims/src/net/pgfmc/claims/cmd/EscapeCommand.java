package net.pgfmc.claims.cmd;

import java.util.ArrayList;
import java.util.List;

import net.pgfmc.claims.ownable.Ownable;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

public class EscapeCommand extends PlayerCommand {

	public EscapeCommand() {
		super("escape");
	}

	@Override
	public boolean execute(PlayerData arg0, String arg1, String[] arg2) {
		/*
		Vector4 player_location = new Vector4(arg0.getPlayer().getLocation());
		
		OwnableBlock ob = ClaimsTable.getRelevantClaim(player_location);
		
		if (ob == null) {
			arg0.sendMessage("§cYou aren't in a claim!");
			return true;
		}
		
		Vector4 block_location = ob.getLocation();
		
		int[] loc =  { player_location.x() - block_location.x(), player_location.z() - block_location.z() };
		
		if (loc[0] > loc[1]) {
			double multiplier = ((float) loc[0]) / 37.0;
			int new_x = 37;
			int new_z = (int) Math.ceil(multiplier * loc[1]);
			
			
			
		}
		
		*/
		
		return true;
	}

	@Override
	public List<String> tabComplete(PlayerData arg0, String arg1, String[] arg2) {
		return new ArrayList<>();
	}

	
	public Vector4 getNearestClaimEdge(PlayerData player, Ownable claim) {
		
		return null;
	}
	
	
}
