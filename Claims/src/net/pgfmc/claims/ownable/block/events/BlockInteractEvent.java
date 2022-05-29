package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
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
				
				if (e.getMaterial() != null && e.getMaterial().toString().contains("BUCKET")) { // Disables Bucket placing in claims
					
					Claim claim = ClaimsTable.getRelevantClaim(new Vector4(block));
					
					if (claim != null && claim.getAccess(pd) == Security.BLOCKED) {
						pd.sendMessage("§cThis land is claimed.");
						e.setCancelled(true);
						return;
					}
				}
				
				Claim cont = Claim.getOwnable(block);
				
				if (cont != null) { // if block is a container
					
					switch(cont.getAccess(pd)) {
					case BLOCKED:
						e.setCancelled(true);
						
						switch(block.getType()) {
						
						case BARREL: pd.sendMessage("§cThis barrel is locked!"); return;
						case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is locked!"); return;
						case BREWING_STAND: pd.sendMessage("§cThis brewing stand is locked!"); return;
						case CHEST: pd.sendMessage("§cThis chest is locked!"); return;
						case DISPENSER: pd.sendMessage("§cThis dispenser is locked!"); return;
						case DROPPER: pd.sendMessage("§cThis dropper is locked!"); return;
						case FURNACE: pd.sendMessage("§cThis furnace is locked!"); return;
						case HOPPER: pd.sendMessage("§cThis hopper is locked!"); return;
						case SHULKER_BOX: pd.sendMessage("§cThis shulker box is locked!"); return;
						case SMOKER: pd.sendMessage("§cThis smoker is locked!"); return;
						case BEACON: pd.sendMessage("§cThis beacon is locked!"); return;
						default:
							return;
						}
					case EXCEPTION:
						e.setCancelled(true);
						
						switch(block.getType()) {
						
						case BARREL: pd.sendMessage("§cThis barrel is locked!"); return;
						case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is locked!"); return;
						case BREWING_STAND: pd.sendMessage("§cThis brewing stand is locked!"); return;
						case CHEST: pd.sendMessage("§cThis chest is locked!"); return;
						case DISPENSER: pd.sendMessage("§cThis dispenser is locked!"); return;
						case DROPPER: pd.sendMessage("§cThis dropper is locked!"); return;
						case FURNACE: pd.sendMessage("§cThis furnace is locked!"); return;
						case HOPPER: pd.sendMessage("§cThis hopper is locked!"); return;
						case SHULKER_BOX: pd.sendMessage("§cThis shulker box is locked!"); return;
						case SMOKER: pd.sendMessage("§cThis smoker is locked!"); return;
						case BEACON: pd.sendMessage("§cThis beacon is locked!"); return;
						default:
							return;
						}
					default:
						return;
					
					}
				}
			}
		}
	}
}