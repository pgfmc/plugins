package net.pgfmc.claims.ownable.entities;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && e.getEntity() != null) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				PlayerData pd = PlayerData.from(player);
				
				Claim beacon = ClaimsTable.getClosestClaim(new Vector4(player.getLocation()), Range.PROTECTED);
				
				if (beacon != null && beacon.getAccess(pd) == Security.BLOCKED) {
					player.sendMessage(ChatColor.RED + "Cannot tame on claimed land.");
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
