package net.pgfmc.claims.ownable.block.events;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.ClaimConfigInventory;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.vector4.Vector4;

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
			
		Claim cont = Claim.getOwnable(e.getBlock());
		
		// removes the ownable if able to
		if (cont != null) {
			
			Security s = cont.getAccess(pd);
			
			switch(s) {
			case MEMBER:
				e.setCancelled(true);
				pd.sendMessage(ChatColor.RED + "Only " + cont.getPlayer().getRankedName() + ChatColor.RED + " can remove this claim.");
				break;
				
			case ADMIN:
				if (pd.getPlayer().getGameMode() == GameMode.CREATIVE && !pd.getPlayer().isSneaking())
				{
					pd.getPlayer().openInventory(new ClaimConfigInventory(cont, false).getInventory());
					e.setCancelled(true);
				} else {
                    Set<Claim> nearby = ClaimsTable.getNearbyClaims(cont.getLocation(), Range.MERGE);
					cont.remove();
                    for (Claim claim : nearby) {
                        claim.calculateNetwork(true);
                    }

					pd.sendMessage(ChatColor.GREEN + "Claim Removed!");
				}
				
				break;
			case BLOCKED:
			default:
				e.setCancelled(true);
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				break;
			}
			
			return;
		}

        if (e.getBlock().getType() == Material.BEACON) {
            Set<Claim> claims = ClaimsTable.getNearbyClaims(new Vector4(e.getBlock()), Range.PROTECTED);
            for (Claim claim : claims) {
                claim.beacons.removeIf(x -> (new Vector4(e.getBlock()).equals(x))); 
            }
        }
		
		Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
		
		if (claim == null) return;
		
		if (claim.getAccess(pd) == Security.BLOCKED) {
		    pd.sendMessage(ChatColor.RED + "This land is claimed.");
		    e.setCancelled(true);
		    pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
        }

		
		
	}
	
}
