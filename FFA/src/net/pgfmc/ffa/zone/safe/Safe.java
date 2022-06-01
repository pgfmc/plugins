package net.pgfmc.ffa.zone.safe;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Safe {
	
	public void safeZoneDo(EntityDamageByEntityEvent e)
	{
		// No entity should take damage if inside safe zone
		if (ZoneInfo.getZoneFromLocation(e.getEntity().getLocation()) == Zone.Safe) {
			e.setCancelled(true);
		} else return;
		
		
		
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		
		Zone.Safe.switchZone(p);
		
	}

}
