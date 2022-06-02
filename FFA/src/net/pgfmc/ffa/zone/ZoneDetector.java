package net.pgfmc.ffa.zone;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class ZoneDetector implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		
		Zone.switchZoneUnknown(e.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		ZoneInfo.getZoneFromLocation(e.getEntity().getLocation()).zoneDo(e);
	}
	
}