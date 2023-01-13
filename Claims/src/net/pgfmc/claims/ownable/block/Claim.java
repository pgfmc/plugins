package net.pgfmc.claims.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.pgfmc.claims.ownable.block.table.ClaimSection;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;

/**
 * Block Ownable Class. 
 * 
 * When a new Ownable Block (OB) is created, it is automatically added to the Ownable table and The Sets in 
 * {@code BlockManager} If the block placed is a {@code Lodestone}, then the ownable functions as a claim.
 * if the block is anything else, then it is a normal block Ownable. 
 * 
 * Claims have a square radius of 36 blocks.
 * 
 * Access to any ownable / range inside a claim is determined by the extended {@code Ownable.isAllowed(PlayerData pd)}.
 * @see Ownable
 * @see BlockManager
 * @see ClaimSection 
 * @see ContainerSection
 * @see ClaimsTable
 * @see ContainerTable
 * 
 * @author CrimsonDart
 * @since 1.1.0	
 * @version 4.0.3
 */
public class Claim {
	
	// protected String name;
	private PlayerData placer;
	
	private Vector4 vector;
	
	private Set<PlayerData> members;
	
	/**
	 * Defines access states.
	 * 
	 * Each constant defines a different relationship between 
	 * the owner, the 
	 * 
	 * @author CrimsonDart
	 *
	 */
	public enum Security {
		ADMIN, 
		MEMBER,
		BLOCKED,
	}
	
	public Claim(PlayerData player, Vector4 vec, Set<PlayerData> members) {
		this.placer = player;
		this.members = members;
		
		Block block = vec.getBlock();
		vector = vec;
		
		if (block.getType() == Material.LODESTONE) {
			
			ClaimsTable.put(this);
			return;
		}
	}
	
	
	public void updateFrom(Claim claim) {
		this.members = claim.members;
		this.placer = claim.placer;
	}
	
	public void forwardUpdateFrom(Claim claim, Set<Claim> done) {
		if (done == null) {
			done = new HashSet<Claim>();
		}
		
		if (!done.contains(this)) {
			done.add(this);
		}
		
		updateFrom(claim);
		
		for (Claim clame : ClaimsTable.getNearbyClaims(this.getLocation(), Range.MERGE)) {
			if (!done.contains(clame)) {
				done.add(clame);
				clame.forwardUpdateFrom(claim, done);
			}
		}
	}
	
	/**
	 * Removes this ownable.
	 */
	public void remove() {
		ClaimsTable.remove(this);
		return;
	}
	
	/**
	 * Returns the location, in the form of a vector.
	 * @return The vector location.
	 */
	public Vector4 getLocation() {
		return vector;
	}
	
	/**
	 * Gets an ownable from the input block.
	 * @param block The block to get the Ownable for.
	 * @return The block's ownable.
	 */
	public static Claim getOwnable(Block block) { // gets a container from block
		
		
		if (block.getType() == Material.LODESTONE) 
			return ClaimsTable.getOwnable(new Vector4(block));
		else return null;
	}
	
	public static Claim getOwnable(Vector4 v) {
		return ClaimsTable.getOwnable(v);
	}

	// --------------------------------------------------- getters and setters
	
	/**
	 * Gets the Player that owns this Ownable.
	 * @return The player's PlayerData.
	 */
	public PlayerData getPlayer() {
		return placer;
	}
	
	public void setOwner(PlayerData pd) {
		placer = pd;
	}
	
	public Set<PlayerData> getMembers() {
		return members;
	}
	
	public Security getAccess(PlayerData player) {
		
		if (placer == null) {
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
				return Security.ADMIN;
			} else {
				return Security.BLOCKED;
			}
		}
		
		
		if (player == placer) {
			return Security.ADMIN;
		} else if (members.contains(player)) {
			return Security.MEMBER;
		} else {
			return Security.BLOCKED;
		}
	};
}

















