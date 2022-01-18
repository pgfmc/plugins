package net.pgfmc.core.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.pgfmc.core.CoreMain;

public class DimManager {
	
	/**
	 * Tests if a player can go to a world by checking permissions and if the world exists/is online
	 * @param p Player
	 * @param world World
	 * @return true if the player can go to the world, false if else
	 */
	public static boolean canGotoWorld(Player p, String world)
	{
		FileConfiguration config = CoreMain.plugin.getConfig();
		updateConfigForWorldPermissionAccess();
		
		// checks if the world exists and that the player has the permission needed (false is either is null or false)
		boolean access = config.getBoolean("dim.access." + world);
		boolean permission = p.hasPermission(config.getString("dim.permission." + world));
		
		// Optional since one of the above could give null
		return Optional.ofNullable(access && permission).orElse(false);
	}
	
	/**
	 * Tests if a player is in a specified world, does not test for dimension
	 * 
	 * @author bk
	 * @param current Player's current world, dimensions are okay
	 * @param expected World to check for without dimension
	 * 
	 * @return true if in the world, false if else
	 */
	public static boolean isInWorld(String current, String expected)
	{
		String[] dims = { current, current + "_nether", current + "_the_end" };
		
		for (String dim : dims)
		{
			if (expected.equals(dim))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isInOverworld(World world)
	{
		String name = world.getName();
		if (name.contains("_nether") || name.contains("_the_end")) { return false; }
		
		return true;
	}
	
	public static boolean isInNether(World world)
	{
		String name = world.getName();
		if (name.contains("_nether") && !name.contains("_the_end")) { return true; }
		
		return false;
	}
	
	public static boolean isInEnd(World world)
	{
		String name = world.getName();
		if (!name.contains("_nether") && name.contains("_the_end")) { return true; }
		
		return false;
	}
	
	/**
	 * Use isInWorld(String current, String expected) -- It isn't survival explicit
	 * 
	 * @author CrimsonDart
	 * @param world any World
	 * @return if "world" is a survival world, true. else, false.
	 */
	@Deprecated
	public static boolean isSurvivalWorld(World world) {
		if (world.getName().contains("survival")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns list of all worlds
	 * @param includeDims True to include end and nether dimensions
	 * @return list of all worlds
	 */
	public static List<World> getAllWorlds(boolean includeDims) {
		
		return Bukkit.getWorlds().stream().filter(x -> {
			return (!(x.getName().contains("_nether") || x.getName().contains("_the_end")));
		}).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @return List of all Worlds' names converted from Multiverse worlds (see EssentialsMain.getMultiverseWorlds())
	 */
	public static List<String> getAllWorldNames()
	{
		
		return Bukkit.getWorlds().stream().map(x -> {
			return x.getName();
		}).collect(Collectors.toList());
	}
	
	/**
	 * Updates the config so that any new worlds have an option for permission and access
	 */
	public static void updateConfigForWorldPermissionAccess()
	{
		// note to self, look into config.isSet() -- thx past self
		// Gets all multiverse worlds, filters the worlds that don't have a permission set
		getAllWorldNames()
		.stream().
		filter( world -> Optional
				.ofNullable(CoreMain.plugin.getConfig()
						.get("dim.permission." + world))
				.isEmpty())
		// Continuation of above, for each world, set a default permission (pgf.dim.<world name>)
		.forEach(world -> CoreMain.plugin.getConfig()
				.set("dim.permission." + world, "pgf.dim." + world));
		
		// Gets all multiverse worlds, filters the worlds that don't have an access option set
				getAllWorldNames()
				.stream()
				.filter(world -> Optional
						.ofNullable(CoreMain.plugin.getConfig()
								.get("dim.access." + world))
						.isEmpty())
				// Continuation of above, for each world, set a default access (true)
				.forEach(world -> CoreMain.plugin.getConfig().set("dim.access." + world, true));
				
				CoreMain.plugin.saveConfig();
	}
	
	/**
	 * @author CrimsonDart
	 * @return a World's associated integer
	 * @param world Any world
	 */
	public static int worldToInt(World world) { // converts a world to an int. returns 3 for all exceptions.
		
		if (world == null) {
			return 3;
		} else if (isInOverworld(world)) {
			return 0;
		} else if (isInNether(world)) {
			return 1;
		} else if (isInEnd(world)) {
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
		
		if (integer == 0) {
			return Bukkit.getWorld(world);
			
		} else if (integer == 1) {
			return Bukkit.getWorld(world + "_nether");
			
		} else if (integer == 2 ) {
			return Bukkit.getWorld(world + "_the_end");
			
		}
		return null;
	}

}
