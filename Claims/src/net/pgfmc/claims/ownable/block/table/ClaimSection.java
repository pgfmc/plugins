package net.pgfmc.claims.ownable.block.table;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.util.LongHash;
import net.pgfmc.core.util.vector4.Vector4;

/**
 * Stores claims in a certain region.
 * @see ClaimsTable
 * 
 * Containes additional features that make claims work.
 * (this is where the bounds caclculations are done)
 * 
 * @author CrimsonDart
 * @since 1.4.1
 */
public class ClaimSection {
	
	/* Claim Section size*/
	public static int CSS  = 512;
	
	protected long key;
	protected int w;
	
	private Map<Neighbor, ClaimSection> neighbors = new EnumMap<Neighbor, ClaimSection>(Neighbor.class);
	private Set<Claim> claims = new HashSet<>();
	
	public ClaimSection(long key, int w) {
		this.key = key;
	}
	
	/**
	 * 
	 * @return all claims stored in this Section.
	 */
	public Set<Claim> getAllClaims() {
		return claims;
	}
	
	public static Set<Claim> getClaims(ClaimSection cs, Vector4 v, Range r) {
		
		
		Set<Claim> claims = new HashSet<>();
		
		if (cs == null) return claims;
		if (cs.claims.size() == 0) return claims;
		
		for (Claim c : cs.claims) {
			if (ClaimsLogic.isInRange(c, v, r)) {
				
				claims.add(c);
			}
		}
		
		return claims;
	}
	
	public static Claim getRelevantClaim(ClaimSection cs, Vector4 v, Range r) {
		if (cs == null) return null;
		if (cs.claims.size() == 0) return null;
		
		for (Claim c : cs.claims) {
			if (ClaimsLogic.isInRange(c, v, r)) {
				return c;
			}
		}
		return null;
	}
	
	public Set<Claim> getNearbyClaims(Vector4 v, Range r) {

		final Set<Claim> ob = getClaims(this, v, r);
		int claimRange = r.getRange();

        final int[][] possible_locations = {
            {v.x() + claimRange, v.z() + claimRange},
            {v.x() - claimRange, v.z() + claimRange},
            {v.x() + claimRange, v.z() - claimRange},
            {v.x() - claimRange, v.z() - claimRange},
        };

        for (int[] pair : possible_locations) {
            getClaims(ClaimsTable.getSection(pair[0], pair[1], this.w), v, r).forEach(x -> ob.add(x));
        }

		return ob;
	}
	
	public Claim getOwnable(Vector4 v) {
		if (claims.size() == 0) return null;
		
		for (Claim c : claims) {
			if (c.getLocation().equals(v)) {
				return c;
			}
		}
		return null;
	}
	
	public void put(Claim ob) {
		claims.add(ob);
	}
	
	public void remove(Claim ob) {
		claims.remove(ob);
	}
	
	public ClaimSection getNeighbor(Neighbor n) {
		ClaimSection cs = neighbors.get(n);
		if (cs != null) {
			return cs;
		}
		
		cs = ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w);
		neighbors.put(n, cs);
		return cs;
	}
}
