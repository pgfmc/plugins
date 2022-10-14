package net.pgfmc.claims.ownable.block.events;

import java.util.HashSet;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
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
		PlayerData pd = PlayerData.from(e.getPlayer());
		Block block = e.getBlock();
		
		if (block.getType() == Material.LODESTONE) { // for placing claims
			
			// logic for merging claims: 
			
			Claim merger = ClaimsTable.getClosestClaim(new Vector4(block), Range.MERGE);
			Claim foreign = ClaimsTable.getClosestClaim(new Vector4(block), Range.FOREIGN);
			
			
			
			// Within Merge claim range
			if (merger != null && (merger.getMembers().contains(pd) || merger.getPlayer() == pd)) {
				
				new Claim(merger.getPlayer(), new Vector4(block), merger.getMembers());
				
				pd.sendMessage("§aSurrounding land claimed!");
				pd.sendMessage("§6Claim merged with the nearby claim.");
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				
				
			// Within Foreign claim range	
			} else if (foreign != null && foreign.getAccess(pd) == Security.BLOCKED) {
				e.setCancelled(true);
				pd.sendMessage("§cCannot claim land that would overlap another claim.");
				
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				
			// not within any range
			} else {
				
				new Claim((e.getPlayer().getGameMode() == GameMode.CREATIVE) ?
								null : 
								pd,
						 new Vector4(block), new HashSet<PlayerData>());
				
				
				pd.sendMessage("§aSurrounding land claimed!");
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
			}
			return;
		}
		
		Claim claim = ClaimsTable.getClosestClaim(new Vector4(block), Range.PROTECTED);
		
		if (claim != null && claim.getAccess(pd) == Security.BLOCKED) {
			
			pd.sendMessage("§cCannot place blocks in claimed land.");
			e.setCancelled(true);
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			return;
		}
	}
}