package net.pgfmc.claims.ownable.block.table;

import com.sk89q.worldguard.util.collect.LongHash;
import com.sk89q.worldguard.util.collect.LongHashTable;

import net.pgfmc.claims.ownable.block.OwnableBlock;
import net.pgfmc.core.util.Vector4;

/**
 * Stores all Containers in a hierarchy of data;
 * 
 * Each container is stored in a <ContainerSection>, which stores all claims in a 16 x 16 block region, aligned with each chunk.
 * Each <ContainerSection> in turn is stored in a (very optimized) <LongHashTable> i borrowed (stole) from WorldGuard.
 * 
 * There is a new <LongHashTable> created for each World, for maximum efficiency.
 * 
 * @author CrimsonDart
 * @since 1.4.1
 */
public class ContainerTable {
	
	private static LongHashTable<ContainerSection> Overworldtable = new LongHashTable<>();
	private static LongHashTable<ContainerSection> Nethertable = new LongHashTable<>();
	private static LongHashTable<ContainerSection> Endtable = new LongHashTable<>();
	
	
	private static LongHashTable<ContainerSection> getWorldTable(int w) {

		switch(w) {
		
		case 0: return Overworldtable;
		case 1: return Nethertable;
		case 2: return Endtable;
		
		default: return null;
		}
	}
	
	private static ContainerSection getSection(Vector4 v) {
		return getWorldTable(v.w()).get(getSectionKey(v));
	}
	
	private static void put(ContainerSection cs) {
		getWorldTable(cs.w).put(cs.key, cs);
		return;
	}
	
	public static void put(OwnableBlock ob) {
		
		System.out.println("container pushed");
		
		ContainerSection cs = getSection(ob.getLocation());
		if (cs != null) {
			cs.put(ob);
			
		} else {
			cs = new ContainerSection(getSectionKey(ob.getLocation()), ob.getLocation().w());
			cs.put(ob);
			put(cs);
		}
	}
	
	public static OwnableBlock getOwnable(Vector4 v) {
		ContainerSection cs = getSection(v);
		if (cs != null) {
			return cs.getOwnable(v);
		}
		return null;
	}
	
	/**
	 * Removes this container.
	 * @param ob The container.
	 */
	public static void remove(OwnableBlock ob) {
		ContainerSection cs = getSection(ob.getLocation());
		if (cs != null) {
			cs.remove(ob);
		}
	}
	
	/**
	 * gets the claimSection key (a long) based on the location.
	 * @param v
	 * @return
	 */
	private static long getSectionKey(Vector4 v) {
		return LongHash.toLong(v.x()/16, v.z()/16);
	}
}
