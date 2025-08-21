package net.pgfmc.core.util.vector4;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldNumbered {
	
	/**
	 * @author CrimsonDart
	 * @return a World's associated integer
	 * @param world Any world
	 */
	public static int worldToInt(World world) { // converts a world to an int. returns 3 for all exceptions.
		
		if (world.getName().contains("nether")) {
			return 1;
		} else if (world.getName().contains("the_end")) {
			return 2;
		} else {
			return 0;
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

            if (worldToInt(world) == integer) {
                return world;
            }
        }
        return null;
	}
}
