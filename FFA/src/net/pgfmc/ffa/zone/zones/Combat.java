package net.pgfmc.ffa.zone.zones;

import java.util.Optional;

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
import net.pgfmc.ffa.FFAScoreboard;
import net.pgfmc.ffa.zone.ZoneDo;
import net.pgfmc.ffa.zone.ZoneInfo;
import net.pgfmc.ffa.zone.ZoneInfo.Zone;

public class Combat implements ZoneDo, Listener {
	
	private static final FFAScoreboard SCOREBOARD = new FFAScoreboard();
	
	@Override
	public void zoneDo(EntityDamageByEntityEvent e)
	{
		
		if (!(e.getDamager() instanceof Player && !(e.getDamager() instanceof Projectile)))
		{
			e.setCancelled(true);
			return;
			
			
		} else if (e.getDamager() instanceof Projectile)
		{
			Projectile proj = (Projectile) e.getDamager();
			
			if (!(proj.getShooter() instanceof Player))
			{
				e.setCancelled(true);
				return;
				
			}
			
		}
		
		
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
				} else {
					e.setCancelled(true);
					return;
				}
			} else {
				e.setCancelled(true);
				return;
			}
			
			PlayerData pdDamager = PlayerData.from(damager);
			
			Bukkit.broadcastMessage(pdDamager.getRankedName() + ChatColor.RED + " has slain " + pd.getRankedName() + ChatColor.RED + "!");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
			pdDamager.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
			
			double deaths = (double) Optional.ofNullable(pd.getData("deaths")).orElse(0.0) + 1.0;
			pd.setData("deaths", deaths).queue();;
			
			double kills = (double) Optional.ofNullable(pdDamager.getData("kills")).orElse(0.0) + 1.0;
			pdDamager.setData("kills", kills).queue();;
			
			SCOREBOARD.updateScoreboard();			
		}
	}
	
	// Basically only cancels fall damage
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		if (!(e.getEntity() instanceof Player)) return;
		
		if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) return;
		
		e.setCancelled(true);
	}

}
