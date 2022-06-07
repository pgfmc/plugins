package net.pgfmc.survival;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class PvPEvent implements Listener {
	
	
	
	@EventHandler
	public void hitta(EntityDamageByEntityEvent e) {
		
		
		if (e.getEntity() instanceof Player) {
			PlayerData target = PlayerData.from((OfflinePlayer) e.getEntity());
			if (e.getDamager() instanceof Player) {
					PlayerData damager = PlayerData.from((OfflinePlayer) e.getDamager());
					
					if (damager.hasTag("pvp") && target.hasTag("pvp")) {
						return;
					} else {
						e.setDamage(0);
					}
					
			} else if (e.getDamager() instanceof Projectile) {
				
				Projectile proj = (Projectile) e.getDamager();
				if (proj.getShooter() instanceof OfflinePlayer) {
					
					PlayerData damager = PlayerData.from((OfflinePlayer) proj.getShooter());
					
					if (damager.hasTag("pvp") && target.hasTag("pvp")) {
						return;
					} else {
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
