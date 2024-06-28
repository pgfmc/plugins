package net.pgfmc.claims.ownable.block.table;

import java.util.HashSet;
import java.util.Set;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.util.LongHash;
import net.pgfmc.claims.util.LongHashTable;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.vector4.Vector4;

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
	public static void put(Claim ob) {
		
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
	public static void remove(Claim ob) {
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
	public static Claim getClosestClaim(Vector4 v, Range r) {

		ClaimSection cs = getSection(v);
		if (cs == null) { // if there is no CS, then it creates a new one for the position v.
			cs = new ClaimSection(getSectionKey(v), v.w());
			getWorldTable(v.w()).put(getSectionKey(v), cs);
		}
		
		Claim ob = cs.getClosestClaim(v, r);
        return ob;
	}
	
	public static Set<Claim> getNearbyClaims(Vector4 v, Range r) {
		ClaimSection cs = getSection(v);
		if (cs == null) { // if there is no CS, then it creates a new one for the position v
			cs = new ClaimSection(getSectionKey(v), v.w());
			getWorldTable(v.w()).put(getSectionKey(v), cs);
		}
		
		Set<Claim> ob = cs.getNearbyClaims(v, r);
        return ob;
	}
	
	/**
	 * Gets the Claim at this location.
	 * @param v The location.
	 * @return The claim at this location; Returns "null" if there is no claim.
	 */
	public static Claim getOwnable(Vector4 v) {
		ClaimSection cs = getSection(v);
		if (cs != null) {
			return cs.getOwnable(v);
		}
		return null;
	}
	
	/**
	 * gets the claimSection key (a long) based on the location.
	 * @param v
	 * @return
	 */
	private static long getSectionKey(Vector4 v) {
		return LongHash.toLong(v.x()/256, v.z()/256);
	}
	
	public static Set<Claim> getAllClaims() {
		Set<Claim> allClaims = new HashSet<>();
		
		for (ClaimSection cs : Overworldtable.values()) {
			for (Claim claim : cs.getAllClaims()) {
				allClaims.add(claim);
			}
		}
		
		for (ClaimSection cs : Nethertable.values()) {
			for (Claim claim : cs.getAllClaims()) {
				allClaims.add(claim);
			}
		}
		
		for (ClaimSection cs : Endtable.values()) {
			for (Claim claim : cs.getAllClaims()) {
				allClaims.add(claim);
			}
		}
		
		
		return allClaims;
	}
}
