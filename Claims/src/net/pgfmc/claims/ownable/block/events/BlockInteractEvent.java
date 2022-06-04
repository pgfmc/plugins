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
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasBlock()) {
			Block block = e.getClickedBlock();
			
			// Player is in survival mode
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				
				Claim claim = ClaimsTable.getRelevantClaim(new Vector4(block), Range.PROTECTED);
				if (claim == null) return; 
				Security access = claim.getAccess(pd);
				
				if (e.getMaterial() != null && e.getMaterial().toString().contains("BUCKET")) { // Disables Bucket placing in claims
					
					if (claim.getAccess(pd) == Security.BLOCKED) {
						pd.sendMessage("§cThis land is claimed.");
						e.setCancelled(true);
						return;
					}
				} else if (access == Security.BLOCKED || access == Security.EXCEPTION) {
					
					switch(block.getType()) {
					
					case BARREL: pd.sendMessage("§cThis barrel is locked!"); break;
					case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is locked!"); break;
					case BREWING_STAND: pd.sendMessage("§cThis brewing stand is locked!"); break;
					case CHEST: pd.sendMessage("§cThis chest is locked!"); break;
					case DISPENSER: pd.sendMessage("§cThis dispenser is locked!"); break;
					case DROPPER: pd.sendMessage("§cThis dropper is locked!"); break;
					case FURNACE: pd.sendMessage("§cThis furnace is locked!"); break;
					case HOPPER: pd.sendMessage("§cThis hopper is locked!"); break;
					case SHULKER_BOX: pd.sendMessage("§cThis shulker box is locked!"); break;
					case SMOKER: pd.sendMessage("§cThis smoker is locked!"); break;
					case BEACON: pd.sendMessage("§cThis beacon is locked!"); break;
					default:
						return;
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