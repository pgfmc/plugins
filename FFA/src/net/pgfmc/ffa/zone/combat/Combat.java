package net.pgfmc.ffa.zone.combat;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Combat implements Listener {
	
	@EventHandler
	public void onDamageReceived(EntityDamageByEntityEvent e)
	{
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		
		if (ZoneInfo.getZoneFromLocation(p.getLocation()) != Zone.Combat) return;
		
		// If this damage was going to kill the player
		if (p.getHealth() - e.getFinalDamage() <= 0)
		{
			// Set to full health
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
			p.setSaturation(20f); // ???? look into
			
			p.teleport(new Location(p.getWorld(), 0, 0, 0)); // Spawn/lounge
			
			Player damager = null;
			
			if (e.getDamager() instanceof Player)
			{
				damager = (Player) e.getDamager();
			} else if (e.getDamager() instanceof Projectile)
			{
				Projectile proj = (Projectile) e.getDamager();
				if (proj.getShooter() instanceof Player)
				{
					damager = (Player) proj.getShooter();
				}
			}
			
			// TODO
			
		}
	}

}
