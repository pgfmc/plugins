package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.ClaimConfigInventory;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

/**
Written by CrimsonDart

-----------------------------------

Interact Event.

-----------------------------------
 */
public class BlockInteractEvent implements Listener {
	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // code block for right-clicking on a block.
		
		// controls clicking containers and beacons;
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		// Right click not air
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK 
				|| e.getAction() == Action.PHYSICAL
				|| e.getAction() == Action.LEFT_CLICK_BLOCK
				) && e.hasBlock()) {
			Block block = e.getClickedBlock();
			
			// Player is in survival mode
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				Claim claim = ClaimsTable.getClosestClaim(new Vector4(block), Range.PROTECTED);
				if (claim == null) return; 
				Security access = claim.getAccess(pd);
				
				if (access == Security.BLOCKED) {
					
					switch(block.getType()) {
					
					case BARREL: pd.sendMessage("§cThis barrel is claimed!"); break;
					case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is claimed!"); break;
					case BREWING_STAND: pd.sendMessage("§cThis brewing stand is claimed!"); break;
					case CHEST: pd.sendMessage("§cThis chest is claimed!"); break;
					case DISPENSER: pd.sendMessage("§cThis dispenser is claimed!"); break;
					case DROPPER: pd.sendMessage("§cThis dropper is claimed!"); break;
					case FURNACE: pd.sendMessage("§cThis furnace is claimed!"); break;
					case HOPPER: pd.sendMessage("§cThis hopper is claimed!"); break;
					case SHULKER_BOX: pd.sendMessage("§cThis shulker box is claimed!"); break;
					case SMOKER: pd.sendMessage("§cThis smoker is claimed!"); break;
					case BEACON: pd.sendMessage("§cThis beacon is claimed!"); break;
					default:
						
						if (e.getMaterial() == Material.ITEM_FRAME) {
							pd.sendMessage("§cThis land is claimed!");
							e.setCancelled(true);
							return;
						}
					}
					
					e.setCancelled(true);
				}
			}
			
			if (e.getPlayer().isSneaking()) return;
			if (block != null && block.getType() == Material.LODESTONE) {
				Claim claim = ClaimsTable.getOwnable(new Vector4(block));
				if (claim != null && claim.getAccess(pd) == Security.ADMIN || e.getPlayer().getGameMode() == GameMode.CREATIVE) {
					e.setCancelled(true);
					pd.getPlayer().openInventory(new ClaimConfigInventory(claim).getInventory());
				}
			}
		}
	}
}