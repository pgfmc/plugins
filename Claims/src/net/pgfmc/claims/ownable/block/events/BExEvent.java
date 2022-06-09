package net.pgfmc.claims.ownable.block.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.util.Vector4;

public class BExEvent implements Listener {
	
	@EventHandler
	public void explodeEvent(BlockExplodeEvent e) {
		if (ClaimsTable.getNearbyClaims(new Vector4(e.getBlock()), Range.PROTECTED).size() != 0) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void EexplodeEvent(EntityExplodeEvent e) {
		if (ClaimsTable.getNearbyClaims(new Vector4(e.getLocation()), Range.PROTECTED).size() != 0) {
			e.setCancelled(true);
			return;
		}
	}
}