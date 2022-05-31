package net.pgfmc.ffa.zone;

import org.bukkit.Location;

public enum Zone {
	Safe,
	Combat;
	
	public static Zone getZone(Location loc)
	{
		int x = Math.abs(loc.getBlockX());
		int z = Math.abs(loc.getBlockZ());
		
		if (x > 10 || z > 10) return Zone.Combat;
		
		return Zone.Safe;
	}
}