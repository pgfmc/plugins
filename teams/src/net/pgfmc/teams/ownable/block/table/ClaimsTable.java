package net.pgfmc.teams.ownable.block.table;

import org.bukkit.Bukkit;

import com.sk89q.worldguard.util.collect.LongHash;
import com.sk89q.worldguard.util.collect.LongHashTable;

import net.pgfmc.core.util.Vector4;
import net.pgfmc.teams.ownable.block.OwnableBlock;

/**
 * Stores all Claims in a hierarchy of data;
 * 
 * Each claim is stored in a <ClaimSection>, which stores all claims in a 128 x 128 block region, aligned with chunks.
 * Each <ClaimSection> in turn is stored in a (very optimized) <LongHashTable> i borrowed (stole) from WorldGuard.
 * 
 * There is a new <LongHashTable> created for each World, for maximum efficiency.
 * 
 * @author CrimsonDart
 * @since 1.4.1
 */
public class ClaimsTable {
	
	private static LongHashTable<ClaimSection> Overworldtable = new LongHashTable<>();
	private static LongHashTable<ClaimSection> Nethertable = new LongHashTable<>();
	private static LongHashTable<ClaimSection> Endtable = new LongHashTable<>();
	
	
	private static LongHashTable<ClaimSection> getWorldTable(int w) {
		switch(w) {
		
		case 0: return Overworldtable;
		case 1: return Nethertable;
		case 2: return Endtable;
		
		default: return null;
		}
	}
	
	/**
	 * Gets the section covered by this location.
	 * @param v
	 * @return
	 */
	private static ClaimSection getSection(Vector4 v) {
		return getWorldTable(v.w()).get(getSectionKey(v));
	}
	
	protected static ClaimSection getSection(int x, int z, int w) {
		return getWorldTable(w).get(x, z);
		
	}
	
	/**
	 * Puts this claim into the LongMap.
	 * @param ob The Claim.
	 */
	public static void put(OwnableBlock ob) {
		ClaimSection cs = getSection(ob.getLocation());
		if (cs != null) {
			cs.put(ob);
			
		} else {
			cs = new ClaimSection(getSectionKey(ob.getLocation()), ob.getLocation().w());
			cs.put(ob);
			getWorldTable(cs.w).put(cs.key, cs);
		}
	}
	
	/**
	 * Removes this claim.
	 * @param ob The claim.
	 */
	public static void remove(OwnableBlock ob) {
		ClaimSection cs = getSection(ob.getLocation());
		if (cs != null) {
			cs.remove(ob);
		}
	}
	
	/**
	 * Gets the claim that covers this location.
	 * @param v The location
	 * @return The claim that contains this location. Returns "null" if there is no claim.
	 */
	public static OwnableBlock getRelevantClaim(Vector4 v) {
		ClaimSection cs = getSection(v);
		if (cs == null) { // if there is no CS, then it creates a new one for the position v.
			Bukkit.getLogger().warning("cs was null, creating new cs for GRC.");
			cs = new ClaimSection(getSectionKey(v), v.w());
			getWorldTable(v.w()).put(getSectionKey(v), cs);
		}
		
		OwnableBlock ob = cs.getClosestClaim(v);
		if (ob != null) {
			return ob;
		}
		
		return null;
	}
	
	/**
	 * Gets the Claim at this location.
	 * @param v The location.
	 * @return The claim at this location; Returns "null" if there is no claim.
	 */
	public static OwnableBlock getOwnable(Vector4 v) {
		ClaimSection cs = getSection(v);
		if (cs != null) {
			return cs.getOwnable(v);
		}
		return null;
	}
	
	public static boolean isOverlappingClaim(Vector4 v) {
		ClaimSection cs = getSection(v);
		if (cs == null) {
			Bukkit.getLogger().warning("cs was null, creating a new cs for IOC.");
			cs = new ClaimSection(getSectionKey(v), v.w());
			getWorldTable(v.w()).put(getSectionKey(v), cs);
		}
		
		return cs.isOverlappingClaim(v);
	}
	
	/**
	 * gets the claimSection key (a long) based on the location.
	 * @param v
	 * @return
	 */
	private static long getSectionKey(Vector4 v) {
		return LongHash.toLong(v.x()/128, v.z()/128);
	}
}
