package net.pgfmc.claims.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.Claim.Security;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;

public class BucketEvent implements Listener {
	
	
	@EventHandler
	public void emptyEvent(PlayerBucketEmptyEvent e) {
		bucketEvent(e);
	}
	
	@EventHandler
	public void fillEvent(PlayerBucketFillEvent e) {
		bucketEvent(e);
	}
	
	public void bucketEvent(PlayerBucketEvent e) {
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getBlock()), Range.PROTECTED);
			if (claim == null) return; 
			Security access = claim.getAccess(pd);
			
			if (access == Security.BLOCKED) {
				e.setCancelled(true);
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				return;
			}
		}
	}
	
	@EventHandler 
	public void bucketEntityEvent(PlayerBucketEntityEvent e) {
		
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			
			Claim claim = ClaimsTable.getClosestClaim(new Vector4(e.getEntity().getLocation()), Range.PROTECTED);
			if (claim == null) return; 
			Security access = claim.getAccess(pd);
			
			if (access == Security.BLOCKED) {
				e.setCancelled(true);
				pd.sendMessage(ChatColor.RED + "This land is claimed!");
				return;
			}
		}
	}
}
