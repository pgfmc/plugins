package net.pgfmc.core;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * 3 dimensional vector class.
 * Used for Claims and Containers
 * @author CrimsonDart
 *
 */
public class Vector4 implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1969343256789187030L;
	
	private int w;
	private int x;
	private int y;
	private int z;
	
	/**
	 * Creates a new Vector3
	 */
	public Vector4(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new Vector3 from a Bukkit Location
	 */
	public Vector4(Location loc) {
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.w = DimManager.worldToInt(loc.getWorld());
	}
	
	/**
	 * Creates a new Vector3 from a Bukkit block.
	 */
	public Vector4(Block block) {
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.w = DimManager.worldToInt(block.getWorld());
	}
	
	/**
	 * X coordinate.
	 * @return X
	 */
	public int x() {
		return x;
	}
	
	/**
	 * Y coordinate.
	 * @return Y
	 */
	public int y() {
		return y;
	}
	
	/**
	 * Z coordinate.
	 * @return Z
	 */
	public int z() {
		return z;
	}
	
	/**
	 * Integer representation of the world.
	 */
	public int w() {
		return w;
	}
	
	/**
	 * Returns the vector's world.
	 * @return World
	 */
	public World world() {
		return DimManager.intToWorld(w, "survival");
	}
	
	/**
	 * Gets this vector in the form of a location.
	 * @return
	 */
	public Location toLocation() {
		return new Location(world(), x, y, z);
	}
	
	public Block getBlock() {
		return toLocation().getBlock();
	}
	
	/**
	 * Returns the serialized version of this Vector3.
	 */
	@Override
	public String toString() {
		return "w" + String.valueOf(w) + "x" + String.valueOf(x) + "y" + String.valueOf(y) + "z" + String.valueOf(z);
	}
	
	/**
	 * Takes a serialized Vector3 and returns a Vector3.
	 * @param s Serialized Vector3
	 * @return Vector3 from <s>.
	 */
	public static Vector4 fromString(String s) {
		
		String[] wx = s.replace("w", "").split("x", 2);
		String[] xy = wx[1].split("y", 2);
		String[] yz = xy[1].split("z", 2);
		
		return new Vector4(Integer.parseInt(xy[0]), Integer.parseInt(yz[0]), Integer.parseInt(yz[1]), Integer.parseInt(wx[0]));
	}
	
	@Override
	public boolean equals(Object ob) {
		
		if (ob instanceof Vector4) {
			Vector4 vec = (Vector4) ob;
			return (vec.x == x && vec.y == y && vec.z == z && vec.w == w);
		}
		return false;
	}
	
	public Vector4 add(int x, int y, int z) {
		return new Vector4(this.x + x, this.y + y, this.z + z, w);
	}
}
