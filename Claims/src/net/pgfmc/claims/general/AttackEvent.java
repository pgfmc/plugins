package net.pgfmc.claims.general;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.claims.ownable.entities.OwnableEntity;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * class to route events to different locations to organise better
 * @author CrimsonDart
 * @since 1.2.0
 * 
 */
public class AttackEvent implements Listener {
	
	@EventHandler
	public void attackEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Player) {
			Player attacker = (Player) e.getDamager();
			
			if (e.getEntity() instanceof InventoryHolder && OwnableEntity.getContainer(e.getEntity().getUniqueId()) != null)  {
				
				switch(OwnableEntity.getContainer(e.getEntity()).getAccess(PlayerData.from(attacker))) {
				case OWNER: return;
				case FAVORITE: return;
				case FRIEND: return;
				default: {
					e.setCancelled(true);
					return;
				}
				}
			}
		}
	}
}
