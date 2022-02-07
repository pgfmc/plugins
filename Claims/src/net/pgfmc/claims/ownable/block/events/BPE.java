package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.claims.ownable.Ownable.Security;
import net.pgfmc.claims.ownable.block.BlockManager;
import net.pgfmc.claims.ownable.block.OwnableBlock;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

/*
Written by CrimsonDart

-----------------------------------

Block Place Event.

-----------------------------------
 */

public class BPE implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		Block block = e.getBlock();
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			System.out.println("block placed!");
			
			if (block.getType() == Material.LODESTONE) { // for placing claims
				if (ClaimsTable.isOverlappingClaim(new Vector4(block))) {
					e.setCancelled(true);
					pd.sendMessage("§cCannot claim land that would overlap another claim.");
					
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				} else {
					BlockManager.createBlockContainer(pd, block);
					pd.sendMessage("§aSurrounding land claimed!");
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				}
				return;
			}
			
			OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(block));
			
			if (claim != null && claim.getAccess(pd) == Security.DISALLOWED) {
				
				pd.sendMessage("§cCannot place blocks in claimed land.");
				e.setCancelled(true);
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				return;
			}
			
			// registers block as a container if it is a valid container.
			if (BlockManager.isOwnable(block.getType())) {
				BlockManager.createBlockContainer(pd, block);
				System.out.println("ownable placed!");
			}
		}	
	}
}