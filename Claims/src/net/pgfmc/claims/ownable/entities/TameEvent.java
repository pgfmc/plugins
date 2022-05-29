package net.pgfmc.claims.ownable.entities;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && e.getEntity() != null) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				PlayerData pd = PlayerData.from(player);
				
				Claim beacon = ClaimsTable.getRelevantClaim(new Vector4(player.getLocation()));
				
				if (beacon != null && beacon.getAccess(pd) == Security.BLOCKED) {
					player.sendMessage("§cCannot tame on claimed land.");
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
