package net.pgfmc.claims.ownable.block.events;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.util.vector4.Vector4;

public class BExEvent implements Listener {
	
	@EventHandler
	public void explodeEvent(BlockExplodeEvent e) {
        for (Block block : e.blockList()) {
            Claim claim = ClaimsTable.getClosestClaim(new Vector4(block), Range.PROTECTED);

            if (claim == null || claim.explosionsEnabled) {
                continue;
            }

            e.setCancelled(true);
            return;
        }
	}
	
	@EventHandler
	public void EexplodeEvent(EntityExplodeEvent e) {
        for (Block block : e.blockList()) {
            Claim claim = ClaimsTable.getClosestClaim(new Vector4(block), Range.PROTECTED);

            if (claim == null || claim.explosionsEnabled) {
                continue;
            }

            e.setCancelled(true);
            return;
        }
	}
}
