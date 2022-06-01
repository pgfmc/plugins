package net.pgfmc.ffa.zone;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.ffa.zone.ZoneInfo.Zone;
import net.pgfmc.ffa.zone.combat.Combat;
import net.pgfmc.ffa.zone.safe.Safe;

public class ZoneDetector implements Listener {
	
	Safe safe = new Safe();
	Combat combat = new Combat();
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		
		Zone.switchZoneUknown(e.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		safe.safeZoneDo(e);
		combat.combatZoneDo(e);
	}
	
}