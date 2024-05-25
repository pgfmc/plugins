package net.pgfmc.core.util.vector4;

import java.util.List;

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
	public static World intToWorld(int integer) { // the inverse operation of worldToInt

        List<World> worlds = Bukkit.getWorlds();

        for (World world : worlds) {
            Environment env = world.getEnvironment();
            
            if ((integer == 0 && env == Environment.NORMAL) || 
                    (integer == 1 && env == Environment.NETHER) || 
                    (integer == 2 && env == Environment.THE_END)) 
            {
                return world;
            }
        }
	    return null;	
	}

}
