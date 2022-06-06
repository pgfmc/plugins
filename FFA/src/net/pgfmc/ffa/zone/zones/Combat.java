package net.pgfmc.ffa.zone.zones;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
		Zone zone = ZoneInfo.getZoneFromLocation(e.getEntity().getLocation());
		
		if (zone != Zone.COMBAT) return;
		
		// No fall damage
		if (e.getCause() == DamageCause.FALL)
		{
			e.setCancelled(true);
			return;
		}
		
		// if damager/projectile isn't from a player
		if (!(e.getDamager() instanceof Player
				|| (e.getDamager() instanceof Projectile && ((Projectile) e.getDamager()).getShooter() instanceof Player)))
		{
			e.setCancelled(true);
			return;
		}
		
		Player p = (Player) e.getEntity();
		PlayerData pd = PlayerData.from(p);
		
		// If this damage wasn't going to kill the player
		if (p.getHealth() - e.getFinalDamage() > 0) return;
		// Damage was going to kill player -> ->
		
		// Set to full health and teleport to spawn
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		p.teleport(new Location(p.getWorld(), 0, 104, 0)); // Spawn/lounge coords
		
		Player damager;
		
		// Save to assume damage came from either a player or a projectile from a player
		if (e.getDamager() instanceof Player)
		{
			damager = (Player) e.getDamager();
		} else
		{
			damager = (Player) ((Projectile) e.getDamager()).getShooter();
		}
		
		PlayerData pdDamager = PlayerData.from(damager);
		
		Bukkit.broadcastMessage(pdDamager.getRankedName() + ChatColor.RED + " has slain " + pd.getRankedName() + ChatColor.RED + "!");
		
		// Sound effects
		pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
		pdDamager.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
		
		// Update deaths for entity
		double deaths = (double) Optional.ofNullable(pd.getData("deaths")).orElse(0.0) + 1.0;
		pd.setData("deaths", deaths).queue();;
		
		// Update kills for damager
		double kills = (double) Optional.ofNullable(pdDamager.getData("kills")).orElse(0.0) + 1.0;
		pdDamager.setData("kills", kills).queue();;
		
		SCOREBOARD.updateScoreboard();
		
	}

}
