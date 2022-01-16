package net.pgfmc.teams.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import net.pgfmc.core.Vector4;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.Main;
import net.pgfmc.teams.ownable.Ownable.Lock;

/**
 * Contains Static methods to manage Block Ownables. also contains all containers and claims.
 * @author CrimsonDart
 * @since 1.2.0	
 * @version 4.0.2
 */
public class BlockManager {
	
	protected static Set<OwnableBlock> containers = new HashSet<>();
	protected static Set<OwnableBlock> claims = new HashSet<>();

	public static void createBlockContainer(PlayerData player, Block block) { // a router between Beacons and BlockContainer
		
		
		if (block.getType() == Material.CHEST || block.getType() == Material.CHEST) {
			
			new BukkitRunnable() { // new java runnable :()
				public void run() {
					
					if (block.getType() == Material.CHEST || block.getType() == Material.CHEST) {
						
						Vector4 v = OwnableBlock.getOtherSide(block);
						if (v != null) {
							Block v4 = v.getBlock();
							OwnableBlock cont = OwnableBlock.getOwnable(v4);
			        		if (cont != null) {
			        			
			        			switch (cont.getAccess(player)) {
			        			
			        			case OWNER:
			        				new OwnableBlock(player, new Vector4(block), cont.getLock());
			        				return;
			        			case FAVORITE:
			        				new OwnableBlock(player, new Vector4(block), cont.getLock());
			        			case FRIEND:
			        				new OwnableBlock(cont.getPlayer(), new Vector4(block), cont.getLock());
			        				return;
			        			default:
			        				return;
			        				
			        			}
			        		} else if (v4.getType() == Material.CHEST || v4.getType() == Material.CHEST) {
			        			
			        			new OwnableBlock(player, v, null);
			        		}
						}
					}
	            }
			}.runTaskLater(Main.plugin, 1);
			
			
		} else {
			
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
				new OwnableBlock(player, new Vector4(block), Lock.CREATIVE);
				return;
			}
			new OwnableBlock(player, new Vector4(block), null);
		}
		
		
		
	}
	
	public static Set<OwnableBlock> getClaims() {
		return BlockManager.claims;
	}
	
	public static Set<OwnableBlock> getContainers() {
		return BlockManager.containers;
	}
	
	/**
	 * Returns if the input material is a valid ownable material.
	 * @param type The material to test if it is Ownable.
	 * @return True if the Material is Ownable.
	 */
	public static boolean isOwnable(Material type) {
		return (type == Material.LODESTONE ||
				type == Material.BARREL || 
				type == Material.BLAST_FURNACE || 
				type == Material.BREWING_STAND || 
				type == Material.CHEST || 
				type == Material.DISPENSER || 
				type == Material.DROPPER || 
				type == Material.FURNACE || 
				type == Material.HOPPER || 
				type == Material.SHULKER_BOX || 
				type == Material.SMOKER || 
				type == Material.TRAPPED_CHEST);
	}
}
