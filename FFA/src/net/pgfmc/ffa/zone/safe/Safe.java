package net.pgfmc.ffa.zone.safe;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.ffa.zone.Zone;

public class Safe implements Listener {
	
	@EventHandler
	public void onDamageReceived(EntityDamageByEntityEvent e)
	{
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		
		if (Zone.getZone(p.getLocation()) != Zone.Safe) return;
		
		e.setCancelled(true);
	}

}
