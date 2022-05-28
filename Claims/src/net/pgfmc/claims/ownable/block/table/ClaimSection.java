package net.pgfmc.claims.ownable.block.table;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;

import com.sk89q.worldguard.util.collect.LongHash;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.core.util.Vector4;

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
	
	protected long key;
	protected int w;
	
	private Map<Neighbor, ClaimSection> neighbors = new EnumMap<Neighbor, ClaimSection>(Neighbor.class);
	private Set<Claim> claims = new HashSet<>();
	
	public ClaimSection(long key, int w) {
		this.key = key;
	}
	
	public Set<Claim> getAllClaims() {
		return claims;
	}
	
	public Claim getRelevantClaim(Vector4 v) {
		if (claims.size() == 0) return null;
		
		for (Claim c : claims) {
			Vector4 v1 = c.getLocation();
			
			if (v1.x() - 36 < v.x() &&
					v1.x() + 36 > v.x() &&
					v1.z() - 36 < v.z() &&
					v1.z() + 36 > v.z() &&
					v1.y() - 54 < v.y()) {
				return c;
			}
		}
		return null;
	}
	
	public static Claim getRelevantClaim(ClaimSection cs, Vector4 v) {
		if (cs != null) {
			return cs.getRelevantClaim(v);
		}
		return null;
	}
	
	public Claim getClosestClaim(Vector4 v) {
		
		Claim ob = getRelevantClaim(v);
		if (ob != null) {
			return ob;
		}
		
		int xBound = v.x()%128;
		int zBound = v.z()%128;
		
		if (xBound < 35) {
			
			ob = getRelevantClaim(getNeighbor(Neighbor.LEFT), v);
			if (ob != null) return ob;
			
			if (zBound < 35) {
				
				ob = getRelevantClaim(getNeighbor(Neighbor.DOWN), v);
				if (ob != null) return ob;
				
				else {
					ob = getRelevantClaim(getNeighbor(Neighbor.DOWNLEFT), v);
					if (ob != null) return ob;
				}
				
				return null;
			} else if (zBound > 93) {
				
				ob = getRelevantClaim(getNeighbor(Neighbor.UP), v);
				if (ob != null) return ob;
				
				else {
					ob = getRelevantClaim(getNeighbor(Neighbor.UPLEFT), v);
					if (ob != null) return ob;
				}
			}
			return null;
		} else if (xBound > 93) {
			
			ob = getRelevantClaim(getNeighbor(Neighbor.RIGHT), v);
			if (ob != null) return ob;
			
			if (zBound < 35) {
				
				ob = getRelevantClaim(getNeighbor(Neighbor.DOWN), v);
				if (ob != null) return ob;
				
				else {
					ob = getRelevantClaim(getNeighbor(Neighbor.DOWNRIGHT), v);
					if (ob != null) return ob;
				}
				
				return null;
			} else if (zBound > 93) {
				
				ob = getRelevantClaim(getNeighbor(Neighbor.UP), v);
				if (ob != null) return ob;
				
				else {
					ob = getRelevantClaim(getNeighbor(Neighbor.UPRIGHT), v);
					if (ob != null) return ob;
				}
			}
			return null;
		} else if (zBound < 35) { // move left
			
			ob = getRelevantClaim(getNeighbor(Neighbor.DOWN), v);
			if (ob != null) return ob;
			
		} else if (zBound > 93) {
			ob = getRelevantClaim(getNeighbor(Neighbor.UP), v);
			if (ob != null) return ob;
		}
		return null;
	}
	
	public boolean isOverlappingRange(Vector4 v) {
		for (Claim c : claims) {
			Vector4 v1 = c.getLocation();
			
			if (v1.x() - 72 < v.x() &&
					v1.x() + 72 > v.x() &&
					v1.z() - 72 < v.z() &&
					v1.z() + 72 > v.z()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isOverlappingRange(ClaimSection cs, Vector4 v) {
		if (cs != null) {
			return cs.isOverlappingRange(v);
		}
		return false;
	}
	
	public boolean isOverlappingClaim(Vector4 v) {
		
		if (isOverlappingRange(v)) {
			return true;
		}
		
		int xBound = (v.x() > -1) ? 
				v.x()%128 :
				(-1 * Math.abs(v.x()%128)) + 128;
		
		int zBound = (v.z() > -1) ? 
				v.z()%128 :
				(-1 * Math.abs(v.z()%128)) + 128;
		
		if (!(xBound > 70)) {
			if (isOverlappingRange(getNeighbor(Neighbor.LEFT), v)) return true;
			
			if (!(zBound < 57)) {
				if (isOverlappingRange(getNeighbor(Neighbor.UP), v)) return true;
				if (isOverlappingRange(getNeighbor(Neighbor.UPLEFT), v))return true;
			}
			
			if (!(zBound > 70)) {
				if (isOverlappingRange(getNeighbor(Neighbor.DOWN), v)) return true;
				if (isOverlappingRange(getNeighbor(Neighbor.DOWNLEFT), v)) return true;
			}
		}
		
		if (!(xBound < 57)) {
			if (isOverlappingRange(getNeighbor(Neighbor.RIGHT), v)) return true;
			
			
			if (!(zBound < 57)) {
				if (isOverlappingRange(getNeighbor(Neighbor.UP), v)) return true;
				if (isOverlappingRange(getNeighbor(Neighbor.UPRIGHT), v)) return true;
			}
			
			if (!(zBound > 70)) {
				if (isOverlappingRange(getNeighbor(Neighbor.DOWN), v)) return true;
				if (isOverlappingRange(getNeighbor(Neighbor.DOWNRIGHT), v)) return true;
			}
		}
		
		return false;
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
		System.out.println("pushed pt. 2");
		claims.add(ob);
	}
	
	public void remove(Claim ob) {
		claims.remove(ob);
	}
	
	public ClaimSection getNeighbor(Neighbor n) {
		
		Bukkit.getLogger().warning("getting neighbor at " + n.toString());
		
		ClaimSection cs = neighbors.get(n);
		if (cs != null) {
			Bukkit.getLogger().warning("Neighbor referenced.");
			return cs;
		}
		
		cs = ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w);
		neighbors.put(n, cs);
		return cs;
	}
	
	/**
	 * updates the claim section, with references to neighboring ClaimSections.
	 */
	public void update() {
		for (Neighbor n : Neighbor.values()) {
			neighbors.put(n, ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w));
			
		}
	}
	
	public void update(Neighbor n) {
		neighbors.put(n, ClaimsTable.getSection(LongHash.msw(key) + n.x(), LongHash.lsw(key) + n.z(), w));
	}
}
