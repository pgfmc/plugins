package net.pgfmc.core.cmd;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.util.DimManager;

/**
 * Sends the player to a specific Multiverse World.
 * @author bk
 *
 */
public class Goto implements CommandExecutor {
	
	/**
	 * saves a player's last location.
	 * @param p Player
	 * @param loc The Player's last Location
	 */
	public static void logBackLocation(OfflinePlayer p, Location loc)
	{
		PlayerData.setData(p, "backLoc", loc);
	}
	
	/**
	 * Gets a player's last Location
	 * @param p Player
	 * @return A player's last Location, null if none
	 */
	public static Location getBackLocation(OfflinePlayer p)
	{
		return Optional.ofNullable((Location) PlayerData.getData(p, "backLoc")).orElse(null);
	}
	
	/**
	 * Loads a player's last location for a world
	 * @param p
	 * @param dest
	 * @return Last location for given world
	 */
	public Location dimLoad(OfflinePlayer p, World dest)
	{
		return Optional.ofNullable((Location) PlayerData.getPlayerData(p).loadFromFile(dest.getName() + ".uuid." + p.getUniqueId())).orElse(null);
	}
	
	/**
	 * Saves the player's location for the world they are in before they teleport out of it
	 * @param player Player to be teleported
	 * @param destination Destination of the player
	 */
	public void dimSave(Player player, Location destination)
	{
		Location current = player.getLocation();
		String playerUUID = player.getUniqueId().toString();
		String currentWorldName = current.getWorld().getName();
		
		PlayerData pd = PlayerData.getPlayerData(player);
		
		if (currentWorldName.equals("hub")) // Need to make sure they spawn at 0, 0 so we will save their coordinates as 0, 0
		{
			current = new Location(current.getWorld(), 0.5, 193.0, 0.5);
		}
		
		// We want the "path" to have the root world name as the MAIN world name (no _nether/_the_end). This information is still saved in <world>.uuid.<uuid>.world
		// The reason for this is because we want to overwrite the saved world with the new world which might be a different dimension WITHOUT creating a new directory for dimensions of a world
		if (currentWorldName.contains("nether")) { pd.saveToFile(currentWorldName.substring(0, currentWorldName.length() - 7) + ".uuid." + playerUUID, current); } // If teleporting from Nether
		if (currentWorldName.contains("the_end")) { pd.saveToFile(currentWorldName.substring(0, currentWorldName.length() - 8) + ".uuid." + playerUUID, current); } // If teleporting from The End
		if (!(currentWorldName.contains("the_end")) && !(currentWorldName.contains("nether"))) { pd.saveToFile(currentWorldName + ".uuid." + playerUUID, current); } // If teleporting from Overworld
		
		logBackLocation(player, current);
		player.teleport(destination); // Teleports sender to the hub if no errors while saving
		player.setVelocity(new Vector());
		
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
		player.sendMessage("§aSent to " + destination.getWorld().getName() + "!");
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { return false; }
		
		if (args.length > 1) { return false; } // /goto <world>
		
		Player p = (Player) sender;
		
		// if /goto (no args)
		if (label.equals(cmd.getName()) && Optional.ofNullable(args.length).isEmpty())
		{
			// TODO open a gui with available worlds :)
			
			return false; // false for now
		}
		
		// if /<world> (no args)
		if (DimManager.getAllWorldNames().contains(label) && Optional.ofNullable(args.length).isEmpty())
		{
			
		} else if (DimManager.getAllWorldNames().contains(label) && Optional.ofNullable(args.length).isPresent())
		{
			// if /<world> (args)
			return false;
		}
		
		// if /goto (args)
		if (args.length == 1)
		{
			if (args[0].contains("_nether") || args[0].contains("_the_end"))
			{
				p.sendMessage("§cYou cannot travel to these dimensions.");
				return true;
			}
			if (DimManager.canGotoWorld(p, args[0]))
			{
				World currentWorld = p.getLocation().getWorld();
				World gotoWorld = Bukkit.getWorld(args[0]);
				
				if (DimManager.isInWorld(currentWorld.getName(), gotoWorld.getName()))
				{
					p.sendMessage("§cYou are already in this world.");
					return true;
				}
				// player, destination
				dimSave(p, Optional.ofNullable(dimLoad(p, gotoWorld)).orElse(gotoWorld.getSpawnLocation()));
				
				return true;
			} else
			{
				p.sendMessage("§cThis world is not available.");
				return true;
			}
		} else
		{
			// This piece of code literally should not run ever
			return false;
		}
		
	}
}
