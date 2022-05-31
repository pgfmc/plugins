package net.pgfmc.claims.ownable.block;

import java.util.Iterator;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

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
public class Claim {
	
	// protected String name;
	private PlayerData placer;
	
	private Vector4 vector;
	
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
		EXCEPTION,
	}
	
	public Claim(PlayerData player, Vector4 vec) {
		this.placer = player;
		
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
	
	public Security getAccess(PlayerData player) {
		return Security.ADMIN;
	};
	
	private static int getYDisplacement(int iter) {
		
		if (iter == 0) {
			return 0;
		} 
		
		if ((iter & 1) == 1) {
			return iter / 2 + 1;
		} else {
			return iter / -2;
		}
	}
	
	// returns the block that a player can be teleported to on the Claim's Edge.
	public Vector4 getNearestClaimEdge(Vector4 player_location) {
		
		if (player_location == null) return null;
		
		Vector4 claim_location = this.getLocation();
		
		int[] loc =  { player_location.x() - claim_location.x(), player_location.z() - claim_location.z() };
		int[] horzEdgeCoords = {0,0};
		
		if (loc[0] > loc[1]) {
			double multiplier = ((float) loc[0]) / 37.0;
			horzEdgeCoords[0] = 36 + claim_location.x();
			horzEdgeCoords[1] = (int) Math.ceil(loc[1] / multiplier) + claim_location.z();
			
		} else {
			double multiplier = ((float) loc[1]) / 37.0;
			horzEdgeCoords[0] = (int) Math.ceil(loc[0] / multiplier) + claim_location.x();
			horzEdgeCoords[1] = 36 + claim_location.z();
		}
		
		Iterator<Integer> iter = IntStream.range(-64, 320).boxed().iterator();
		while (iter.hasNext()) {
			
			Vector4 possibleOut = new Vector4(horzEdgeCoords[0], getYDisplacement(iter.next()) + player_location.y() , horzEdgeCoords[1], player_location.w());
			
			if (isValidPlayerPosition(possibleOut)) {
				return possibleOut.add(0, 1, 0);
			}
		}
		return null;
	}
	
	/**
	 * Checks if the input block can be STOOD ON (not stand in)
	 * @param position
	 * @return
	 */
	private static boolean isValidPlayerPosition(Vector4 position) {
		
		Material standOn = position.getBlock().getType();
		Material lower = position.add(0, 1, 0).getBlock().getType();
		Material upper = position.add(0, 2, 0).getBlock().getType();
		
		return (standOn.isOccluding() && standOn != Material.LAVA && standOn != Material.AIR && canStandIn(lower) && canStandIn(upper));
	}
	
	private static String[] clist = {
			"PRESSURE_PLATE",
			"SIGN",
			"BUTTON",
			"SAPLING",
			"RAIL",
			"CANDLE",
			"TORCH",
			"BANNER",
			"CARPET",
			"CORAL",
			"VINE",
			"ROOT",
			"REDSTONE",
			"DEAD",
			"FERN",
			
			
	};
	
	private static String[] dlist = {
			"_BLOCK",
			"DEEPSLATE"
	};
	
	private static Material[] exactMatch = {
			Material.BROWN_MUSHROOM,
			Material.WATER,
			Material.CARROTS,
			Material.COMPARATOR,
			Material.CRIMSON_FUNGUS,
			Material.WARPED_FUNGUS,
			Material.GRASS
			
	};
	
	private static boolean canStandIn(Material m) {
		if (m == Material.AIR) return true;
		
		for (Material mat : exactMatch) {
			if (m == mat) return true;
		}
		
		for (String s : dlist ) {
			if (m.toString().contains(s)) return false;
		}
		
		for (String s : clist ) {
			if (m.toString().contains(s)) return true;
		}
		
		return false;
	}
}

















