package net.pgfmc.claims.ownable.inspector;

import java.util.List;

import org.bukkit.ChatColor;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.core.util.vector4.Vector4;

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
			pd.sendMessage(ChatColor.GREEN + "Teleported to the active claim!");
		} else {
			pd.sendMessage(ChatColor.GOLD + "No claim in range.");
		}
		return false;
	}
}
