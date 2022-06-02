package net.pgfmc.ffa.zone.zones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.ffa.zone.ZoneDo;
import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Combat implements ZoneDo, Listener {
	
	@Override
	public void zoneDo(EntityDamageByEntityEvent e)
	{
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		PlayerData pd = PlayerData.from(p);
		
		if (ZoneInfo.getZoneFromLocation(p.getLocation()) != Zone.COMBAT) return;
		
		// If this damage was going to kill the player
		if (p.getHealth() - e.getFinalDamage() <= 0)
		{
			// Set to full health
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
			p.setSaturation(20f); // ???? look into
			
			p.teleport(new Location(p.getWorld(), 0, 104, 0)); // Spawn/lounge
			
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
			
			if (damager == null)
			{
				e.setCancelled(true);
				return;
			}
			
			PlayerData pdDamager = PlayerData.from(damager);
			
			Bukkit.broadcastMessage(pdDamager.getRankedName() + ChatColor.RED + " has slain " + pd.getRankedName() + ChatColor.RED + "!");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
			pdDamager.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
			
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) return;
		if (!(e.getEntity() instanceof Player)) return;
		
		e.setCancelled(true);
	}

}
