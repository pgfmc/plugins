package net.pgfmc.claims.ownable.block.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class EntityInteractEvent implements Listener {
	
	@EventHandler
	public void entityEvent(PlayerInteractEntityEvent e) {
		
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getRightClicked().getLocation()), Range.PROTECTED);
			if (claim == null) return; 
			Security access = claim.getAccess(pd);
			
			if (access == Security.BLOCKED && !claim.livestockKilling) {
				e.setCancelled(true);
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				return;
			}
		}
	}
}
