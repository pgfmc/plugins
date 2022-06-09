package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.Vector4;

/**
 * Class that manages Block breaks.
 * 
 * @author CrimsonDart
 * @since 1.0.0
 * @version 4.0.2
 */
public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			Claim cont = Claim.getOwnable(e.getBlock());
			
			// removes the ownable if able to
			if (cont != null) {
				
				Security s = cont.getAccess(pd);
				
				switch(s) {
				case BLOCKED:
					e.setCancelled(true);
					pd.sendMessage("§cThis land is claimed!");
					return;
				case MEMBER:
					
					e.setCancelled(true);
					pd.sendMessage("§cOnly " + cont.getPlayer().getRankedName() + " §r§ccan remove this claim.");
					return;
					
				case ADMIN:
					cont.remove();
					pd.sendMessage("§aClaim Removed!");
					return;
					
				}
			}
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
			
			if (claim != null) {
				
				if (claim.getAccess(pd) == Security.BLOCKED) {
					pd.sendMessage("§cThis land is claimed.");
					e.setCancelled(true);
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					return;
				}
			}
		} else if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			
			if (pd.getTags().contains("inspector")) {
				e.setCancelled(true);
				
				Claim ob = Claim.getOwnable(e.getBlock());
				
				if (ob != null) {
					pd.sendMessage("§eOwnableBlock data from: §c" + ob.getLocation().toString());
					pd.sendMessage("§eOwner: §d" + ob.getPlayer().getRankedName());
					pd.setData("OwnableCache", ob);
					pd.sendMessage("§dOwnable Selected!");
					return;
				}
				
				Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
				
				if (claim != null) {
					pd.sendMessage("§bInside claim at §c" + claim.getLocation().toString());
					pd.sendMessage("§bOwnber: §d" + claim.getPlayer().getRankedName());
				} else {
					pd.sendMessage("§bNot Inside a claim.");
				}
			}
			
			Claim cont = Claim.getOwnable(e.getBlock());
			if (cont != null) {
				cont.remove();
				pd.sendMessage("§aClaim Removed!");
				return;
			}
		}
	}
}