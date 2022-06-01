package net.pgfmc.ffa.zone;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class ZoneDetector implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		
		Zone.switchZoneUknown(e.getPlayer());
	}
	
}