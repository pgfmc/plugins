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
		// No entity should take damage if inside safe zone
		if (ZoneInfo.getZoneFromLocation(e.getEntity().getLocation()) == Zone.SAFE) {
			e.setCancelled(true);
		} else return;
		
		
		
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		
		Zone.SAFE.switchZone(p);
		
	}

}
