package net.pgfmc.core.util.vector4;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;

public class WorldNumbered {
	
	/**
	 * @author CrimsonDart
	 * @return a World's associated integer
	 * @param world Any world
	 */
	public static int worldToInt(World world) { // converts a world to an int. returns 3 for all exceptions.
		
		if (world.getEnvironment() == Environment.NORMAL) {
			return 0;
		} else if (world.getEnvironment() == Environment.NETHER) {
			return 1;
		} else if (world.getEnvironment() == Environment.THE_END) {
			return 2;
		} else {
			return 3;
		}
		
	}
	
	/**
	 * @author CrimsonDart
	 * @return an integer's associated World
	 * @param integer any integer 0 - 2
	 */
	public static World intToWorld(int integer, String world) { // the inverse operation of worldToInt
		
		switch(integer) {
		case 0:
			return Bukkit.getWorld(world);
		case 1:
			return Bukkit.getWorld(world + "_nether");
		case 2:
			return Bukkit.getWorld(world + "_the_end");
		default:
			return null;
		}
		
	}

}
