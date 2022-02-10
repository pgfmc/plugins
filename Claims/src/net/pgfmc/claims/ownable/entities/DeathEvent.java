package net.pgfmc.claims.ownable.entities;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathEvent implements Listener {
	
	@EventHandler
	public void deathEvent(EntityDeathEvent e) {
		
		if (OwnableEntity.getContainer(e.getEntity()) != null ) {
			Bukkit.getLogger().warning("Entity Container deleted!");
			OwnableEntity.remove(e.getEntity());
		}
	}
}
