package net.pgfmc.claims.ownable.block;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.pgfmc.claims.ownable.Ownable;
import net.pgfmc.claims.ownable.block.table.ClaimSection;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.playerdataAPI.PlayerData;
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
public class Claim extends Ownable {
	
	private Vector4 vector;
	
	public Claim(PlayerData player, Vector4 vec) {
		super(player);
		
		Block block = vec.getBlock();
		vector = vec;
		
		if (block.getType() == Material.LODESTONE) {
			
			ClaimsTable.put(this);
			return;
		}
	}
	
	/**
	 * Removes this ownable.
	 */
	public void remove() {
		Bukkit.getLogger().warning("1");
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
}
