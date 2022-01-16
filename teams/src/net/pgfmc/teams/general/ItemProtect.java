package net.pgfmc.teams.general;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;
 /**
  * Prevents other players from picking up someone else's items on death.
  * Effect fades after 2 minutes.
  * @author bk + CrimsonDart
  * @since 1.3.0
  */
public class ItemProtect implements Listener {
	
	private static HashMap<Item, Entry> items = new HashMap<>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		// declare variables
		Player p = e.getEntity();
		Location loc = p.getLocation();
		World world = p.getWorld();
		PlayerData pd = PlayerData.getPlayerData(p);
		
		// converts dropped items
		List<Item> droppedItems = e.getDrops().stream().map((x -> {
			return world.dropItem(loc, x);
		})).collect(Collectors.toList());
		
		// Drop a custom player head if death by charged creeper
		if (p.getLastDamageCause().getEntityType().equals(EntityType.CREEPER))
		{
			Creeper creeper = (Creeper) p.getLastDamageCause().getEntity();
			if (creeper.isPowered())
			{
				droppedItems.add(world.dropItem(loc, Skull.getHead(p.getUniqueId(), null)));
			}
		}
		
		// activates the items
		for (Item drop : droppedItems) {
			
			drop.setGlowing(true);
			drop.setInvulnerable(true);
			drop.setVelocity(new Vector());
			
			items.put(drop, new Entry(pd, drop));
		}
		
		// starts timer for deactivating the items
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() {
			
			@Override
			public void run()
			{
				for (Item drop : droppedItems) {
					Entry data = items.get(drop);
					
					if (data != null) {
						data.remove();
					}
					
				}
			}
			
		}, 2400); // two minutes
		
		e.getDrops().clear();
		p.sendMessage("§dYour dropped items are protected for 2 minutes.");
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		
		if (e.getEntity() instanceof OfflinePlayer) {
			Item item = e.getItem();
			
			
			Entry data = items.get(item);
			
			if (data != null) {
				
				PlayerData pd = PlayerData.getPlayerData((OfflinePlayer) e.getEntity());
				
				e.setCancelled(!(data.pickup(pd)));
				
			}
		}
	}
	
	/**
	 * An entry for each Item dropped by a player after death.
	 * @author james
	 *
	 */
	private static class Entry {
		
		PlayerData pd;
		Item drop;
		
		protected Entry(PlayerData pd, Item drop) {
			
			this.pd = pd;
			this.drop = drop;
		}
		
		public boolean pickup(PlayerData picker) {
			
			if (pd == picker) {
				remove();
				return true;
			} else if (Friends.getFriendsMap(picker).containsKey(pd)) {
				remove();
				return true;
			} else {
				return false;
			}
		}
		
		public void remove() {
			items.remove(drop);
			
			if (drop.isValid()) {
				drop.setGlowing(false);
				drop.setInvulnerable(false);
			}
		}
		
		
	}

}
