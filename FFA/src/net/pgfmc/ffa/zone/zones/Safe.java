package net.pgfmc.ffa.zone.zones;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.ffa.zone.ZoneDo;
import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Safe implements ZoneDo {
	
	@Override
	public void zoneDo(EntityDamageByEntityEvent e)
	{
		Zone zone = ZoneInfo.getZoneFromLocation(e.getEntity().getLocation());
		
		if (zone != Zone.SAFE) return;
		
		// Let damage through if the damager is in creative mode
		if (e.getDamager() instanceof Player)
		{
			Player pDamager = (Player) e.getDamager();
			
			if (pDamager.getGameMode() == GameMode.CREATIVE) return;
		}
		
		// Cancel all damage
		e.setCancelled(true);
		
		if (!(e.getEntity() instanceof Player))
		{
			Player p = (Player) e.getEntity();
			
			if (p.getGameMode() != GameMode.SURVIVAL) return;
			
			// Switch zone inventory if in survival mode
			Zone.SAFE.switchZone(p);
		}
		
	}

}
