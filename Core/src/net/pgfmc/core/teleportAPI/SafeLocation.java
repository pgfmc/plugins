package net.pgfmc.core.teleportAPI;

import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import net.pgfmc.core.util.Vector4;

public class SafeLocation implements Runnable {
	
	public static LinkedList<Material> unsafe = new LinkedList<>(
			Arrays.asList(Material.LAVA, Material.WATER, Material.CACTUS
					, Material.FIRE, Material.WITHER_ROSE, Material.BEDROCK));
	
	// Immediately writed to CPU cache
	// Output loc
	private volatile Location safeLoc;
	
	// Input loc
	private Location loc;
	private Vector4 v;
	
	private final Thread thread;
	
	public SafeLocation(Location loc)
	{
		if (loc == null) throw new NullPointerException();
		
		this.loc = loc;
		v = new Vector4(loc);
		thread = new Thread(this);
		
		// Do we need to start the thread?
		if (isSafe(loc.getBlock()))
		{
			safeLoc = loc;
			return;
		}
		
		System.out.println("Starting thread");
		thread.start();
	}
	
	/**
	 * Checks for a safe block in this pattern:
	 * 
	 */
	@Override
	public void run() {
		// Most of these numbers are (kinda) arbitrary numbers set by me lol
		// This loop controls the y level of the search
		for (int y = v.y(); y < Math.min(loc.getBlockY() + 16, loc.getWorld().getMaxHeight() - 5); y++)
		{
			// This loop controls the radius from the centre
			for (int r = 1; r <= 8; r++)
			{
				// System.out.println("Testing lap: " + x + "; with len: " + len);
				
				// This loop controls which block to look at next
				// It checks all 4 sides in the same loop, so it only needs to loop once
				for (int i = 0; i <= r * 2; i++)
				{
					// SOUTHWEST -> SOUTHEAST
					if (isSafe(v.world().getBlockAt(v.x() - r + i, y, v.z() + r)))
					{
						safeLoc = new Location(v.world(), v.x() - r + i, y, v.z() + r).add(0.5, 0, 0.5);
						System.out.println("Safe location found: " + safeLoc.toString());
						return;
					}
					// System.out.println("SOUTHWEST -> SOUTHEAST failed");
					
					// SOUTHEAST -> NORTHEAST
					if (isSafe(v.world().getBlockAt(v.x() + r, y, v.z() + r - i)))
					{
						safeLoc = new Location(v.world(), v.x() + r, y, v.z() + r - i).add(0.5, 0, 0.5);
						System.out.println("Safe location found: " + safeLoc.toString());
						return;
					}
					// System.out.println("SOUTHEAST -> NORTHEAST failed");
					
					// NORTHEAST -> NORTHWEST
					if (isSafe(v.world().getBlockAt(v.x() + r - i, y, v.z() - r)))
					{
						safeLoc = new Location(v.world(), v.x() + r - i, y, v.z() - r).add(0.5, 0, 0.5);
						System.out.println("Safe location found: " + safeLoc.toString());
						return;
					}
					// System.out.println("NORTHEAST -> NORTHWEST failed");
					
					// NORTHWEST -> SOUTHWEST
					if (isSafe(v.world().getBlockAt(v.x() - r, y, v.z() - r + i)))
					{
						safeLoc = new Location(v.world(), v.x() - r, y, v.z() - r + i).add(0.5, 0, 0.5);
						System.out.println("Safe location found: " + safeLoc.toString());
						return;
					}
					// System.out.println("NORTHWEST -> SOUTHWEST failed");
					
				}
			}
		}
		
		System.out.println("Safe location not found");
		safeLoc = loc;
	}
	
	public Thread getThread()
	{
		System.out.println("Getting SafeLocation thread..");
		return thread;
	}
	
	private boolean isSafe(Block b)
	{
		// System.out.println("Testing block: " + bmid.getX() + ", " + bmid.getY() + ", " + bmid.getZ());
		
		if (unsafe.contains(b.getType())
				|| unsafe.contains(b.getRelative(BlockFace.DOWN).getType())
				|| unsafe.contains(b.getRelative(BlockFace.UP).getType())) return false;
		// System.out.println("Passed contains test");
		if (!(b.isPassable()
				&& b.getRelative(BlockFace.UP).isPassable()
				&& !b.getRelative(BlockFace.DOWN).isPassable())) return false;
		// System.out.println("Passed passable test");
		// if (bup.getLocation().getBlockY() >= v.world().getMaxHeight()) return false;
		
		return true;
	}
	
	public Location getLocation()
	{
		return safeLoc;
	}
}
