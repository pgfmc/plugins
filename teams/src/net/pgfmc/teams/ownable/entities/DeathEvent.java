package net.pgfmc.teams.ownable.entities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeathEvent implements Listener {
	
	@EventHandler
	public void deathEvent(EntityDeathEvent e) {
		
		if (OwnableEntity.getContainer(e.getEntity()) != null ) {
			System.out.println("Entity Container deleted!");
			OwnableEntity.remove(e.getEntity());
		}
	}
}
