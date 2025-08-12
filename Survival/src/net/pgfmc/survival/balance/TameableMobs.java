package net.pgfmc.survival.balance;

import java.util.Objects;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class TameableMobs implements Listener {
	
	@EventHandler
	public void onEntityInteract(EntityDamageByEntityEvent e)
	{
		final Entity entity = e.getEntity();
		final Entity damager = e.getDamager();
		
		if (!(entity instanceof Tameable)) return;
		if ((entity instanceof Wolf) && ((Wolf) entity).isAngry()) return; // Players can damage angry wolfs
		
		final Tameable tameableEntity = (Tameable) entity;
		
		if (tameableEntity.getOwner() == null) return;
		
		if (damager instanceof Projectile)
		{
			final Projectile damagerProjectile = (Projectile) damager;
			final ProjectileSource shooter = damagerProjectile.getShooter();
			
			if (!(shooter instanceof Player)) return;
			if (((Player) shooter).getGameMode() == GameMode.CREATIVE) return;
			if (Objects.equals(tameableEntity.getOwner(), (Player) shooter)) return; // Owner is allowed to damage the tameable entity
			
		} else if (damager instanceof Player)
		{
			if (((Player) damager).getGameMode() == GameMode.CREATIVE) return;
			if (Objects.equals(tameableEntity.getOwner(), (Player) damager)) return; // Owner is allowed to damage the tameable entity
			
		} else return;
		
		e.setCancelled(true);
		
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e)
	{
		final Player player = e.getPlayer();
		final Entity entity = e.getRightClicked();
		
		if (!(entity instanceof Tameable)) return;
		//if (!(entity instanceof Vehicle)) return;
		if (player.getGameMode() == GameMode.CREATIVE) return;
		
		final Tameable tameableEntity = (Tameable) entity;
		
		if (tameableEntity.getOwner() == null) return; // no owner
		if (Objects.equals(tameableEntity.getOwner(), player)) return;
		
		e.setCancelled(true);		
		
	}

}
