package net.pgfmc.claims.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
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
		
		Vector4 player_location = new Vector4(arg0.getPlayer().getLocation());
		
		Claim ob = ClaimsTable.getClosestClaim(player_location, Range.PROTECTED);
		
		if (ob == null) {
			arg0.sendMessage(ChatColor.RED + "You aren't in a claim!");
			return true;
		}
		
		//Vector4 claimEdge = ob.getNearestClaimEdge(player_location);
		
		//if (claimEdge != null) {
			//arg0.sendMessage(claimEdge.toString());
			//arg0.teleport(claimEdge.toLocation());
		//}
		
		
		
		
		return true;
	}

	@Override
	public List<String> tabComplete(PlayerData arg0, String arg1, String[] arg2) {
		return new ArrayList<>();
	}
}
