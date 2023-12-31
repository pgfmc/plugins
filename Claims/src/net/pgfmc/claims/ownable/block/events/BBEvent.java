package net.pgfmc.claims.ownable.block.events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
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
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			Claim cont = Claim.getOwnable(e.getBlock());
			
			// removes the ownable if able to
			if (cont != null) {
				
				Security s = cont.getAccess(pd);
				
				switch(s) {
				case BLOCKED:
					e.setCancelled(true);
					pd.sendMessage(ChatColor.RED + "This land is claimed!");
					return;
					
				case MEMBER:
					e.setCancelled(true);
					pd.sendMessage(ChatColor.RED + "Only " + cont.getPlayer().getRankedName() + ChatColor.RESET + ChatColor.RED + " can remove this claim.");
					return;
					
				case ADMIN:
					cont.remove();
					pd.sendMessage(ChatColor.GREEN + "Claim Removed!");
					return;
					
				}
			}
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
			
			if (claim != null) {
				
				if (claim.getAccess(pd) == Security.BLOCKED) {
					pd.sendMessage(ChatColor.RED + "This land is claimed.");
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
					pd.sendMessage(ChatColor.YELLOW + "OwnableBlock data from: " + ChatColor.RED + ob.getLocation().toString());
					pd.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.LIGHT_PURPLE + ob.getPlayer().getRankedName());
					pd.setData("OwnableCache", ob);
					pd.sendMessage(ChatColor.LIGHT_PURPLE + "Ownable Selected!");
					return;
				}
				
				Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
				
				if (claim != null) {
					pd.sendMessage(ChatColor.AQUA + "Inside claim at " + ChatColor.RED + claim.getLocation().toString());
					pd.sendMessage(ChatColor.AQUA + "Ownber: " + ChatColor.LIGHT_PURPLE + claim.getPlayer().getRankedName());
				} else {
					pd.sendMessage(ChatColor.AQUA + "Not Inside a claim.");
				}
			}
			
			Claim cont = Claim.getOwnable(e.getBlock());
			if (cont != null) {
				cont.remove();
				pd.sendMessage(ChatColor.GREEN + "Claim Removed!");
				return;
			}
		}
	}
}