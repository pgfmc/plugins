package net.pgfmc.survival.balance;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Main;

/**
 * Prevents other players from picking up someone else's items on death.
 * Effect fades after 2 minutes.
 * @author bk + CrimsonDart
 * @since 1.3.0
 */
public class ItemProtect implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		// declare variables
		final Player player = e.getEntity();
		final Location deathLocation = player.getLocation();
		final World world = player.getWorld();
		final PlayerData playerdata = PlayerData.from(player);
		
		// set back location
		playerdata.setData("backLoc", deathLocation);
		
		// convert itemstack to item by dropping the items on the ground (need to clear the original drops)
		List<Item> droppedItems = e.getDrops().stream().map((itemstack -> world.dropItem(deathLocation, itemstack))).collect(Collectors.toList());
		e.getDrops().clear();
		
		// activates the items
		for (Item drop : droppedItems) {
			// "Sets the owner of this item. Other entities will not be able to pickup this item when an owner is set."
			drop.setOwner(playerdata.getUniqueId());
			drop.setGlowing(true);
			drop.setInvulnerable(true);
			drop.setVelocity(new Vector());
			
		}
		
		playerdata.playSound(deathLocation, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
		playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "Your dropped items are protected for 2 minutes.");
		playerdata.sendMessage(ChatColor.LIGHT_PURPLE + "Teleport to your back location; available in the command menu!");
		
		// starts timer for deactivating the items
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				for (Item drop : droppedItems)
				{
					if (!drop.isValid()) continue;
					
					drop.setOwner(null);
					drop.setGlowing(false);
					drop.setInvulnerable(false);
					
				}
				
			}
			
		}, 2400); // two minutes
		
	}
	
}
