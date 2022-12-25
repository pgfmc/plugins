package net.pgfmc.claims.ownable.block.events;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

public class PhysicsEvent implements Listener {
	
	@EventHandler
	public void event(BlockPhysicsEvent e) {
		Block block = e.getSourceBlock();
		
		if (block.getType() != Material.DIRT) return;
			
		Block wheat = block.getRelative(BlockFace.UP);
		if (wheat.getType() != Material.WHEAT) return;
		
		
		Claim claim = ClaimsTable.getClosestClaim(new Vector4(block), Range.PROTECTED);
		
		if (claim == null) return;
		
		Chunk chunk = block.getChunk();
		
		for (Entity entity : chunk.getEntities()) {
			
			if (!(entity instanceof Player)) continue;
			
			Location loc = entity.getLocation();
			
			if (!(loc.getBlockX() == block.getX() 
					&& loc.getBlockZ() == block.getZ()
					&& loc.getBlockY() -1 == block.getY()
					)) continue;
			
			PlayerData pd = PlayerData.from((Player) entity);
			
			Security sec = claim.getAccess(pd);
			
			if (sec != Security.BLOCKED) return;
			
			e.setCancelled(true);
			
			block.setType(Material.FARMLAND);
			
			return;
		}
	}
}
