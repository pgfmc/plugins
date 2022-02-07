package net.pgfmc.claims.ownable.block.table;

/**
 * Enum to represent the 4.5 cardinal directions.
 * 
 * @author CrimsonDart
 * @since 1.4.0	
 */
enum Neighbor {
	
	DOWN(0, -1),
	DOWNLEFT(-1, -1),
	LEFT(-1, 0),
	UPLEFT(-1, 1),
	UP(0, 1),
	UPRIGHT(1, 1),
	RIGHT(1, 0),
	DOWNRIGHT(1, -1);
	
	private int x;
	private int z;
	
	Neighbor(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public int x() { return x; }
	public int z() { return z; }
}
