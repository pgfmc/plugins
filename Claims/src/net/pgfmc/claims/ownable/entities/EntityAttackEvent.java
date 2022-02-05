package net.pgfmc.claims.ownable.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class EntityAttackEvent implements Listener {
	
	
	@EventHandler
	public void damageEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Player && e.getEntity() instanceof InventoryHolder) {
			
			OwnableEntity ent = OwnableEntity.getContainer(e.getEntity());
			
			if (ent != null) {
				
				PlayerData pd = PlayerData.getPlayerData((Player) e.getDamager());
				Entity entity = e.getEntity();
				
				if ((entity.getType() == EntityType.MINECART_CHEST || 
					entity.getType() == EntityType.MINECART_HOPPER ||
					entity.getType() == EntityType.ITEM_FRAME ||
					entity.getType() == EntityType.GLOW_ITEM_FRAME ||
					entity.getType() == EntityType.ARMOR_STAND ||
					entity.getType() == EntityType.HORSE ||
					entity.getType() == EntityType.DONKEY ||
					entity.getType() == EntityType.MULE
					
						// if the entity has an inventory.
					
					)) {
					
					switch(ent.getAccess(pd)) {
					
					case OWNER: {return;}
					case FRIEND: {return;}
					case FAVORITE: return;
					case UNLOCKED: {
						e.setCancelled(true);
						return;
					}
					
					case DISALLOWED: {
						e.setCancelled(true);
						return;
					}
					case EXCEPTION: {
						e.setCancelled(true);
						Bukkit.getLogger().warning("cont.isAllowed() returned Security.EXCEPTION!");
						return;
					}
					}
				}
			}
		}
	}
}
