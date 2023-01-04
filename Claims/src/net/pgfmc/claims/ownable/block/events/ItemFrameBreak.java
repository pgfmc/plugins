package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;

public class ItemFrameBreak implements Listener {
	
	@EventHandler
	public void event(HangingBreakByEntityEvent e) {
		
		if (e.getRemover() instanceof Player) {
			Player p = (Player) e.getRemover();
			
			if (p.getGameMode() == GameMode.SURVIVAL) {
				
				Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getEntity().getLocation()), Range.PROTECTED);
				if (claim == null) return; 
				Security access = claim.getAccess(PlayerData.from(p));
				
				if (access == Security.BLOCKED) {
					p.sendMessage(ChatColor.RED + "This land is claimed!");
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
