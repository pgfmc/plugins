package net.pgfmc.survival.cmd.pvp;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.pgfmc.core.api.playerdata.PlayerData;

public class PvpEvent implements Listener {
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
		if (!(e.getEntity() instanceof Player)) return;
		
		PlayerData target = PlayerData.from((OfflinePlayer) e.getEntity());
		
		if (e.getDamager() instanceof Player)
		{
			PlayerData damager = PlayerData.from((OfflinePlayer) e.getDamager());
			
			if (damager.hasTag("pvp") && target.hasTag("pvp")) return;
				
			e.setDamage(0);
			e.setCancelled(true);
			
			return;
		}
		
		if (e.getDamager() instanceof Projectile)
		{
			Projectile proj = (Projectile) e.getDamager();
			
			if (!(proj.getShooter() instanceof Player)) return;
			
			PlayerData damager = PlayerData.from((OfflinePlayer) proj.getShooter());
			
			if (damager.hasTag("pvp") && target.hasTag("pvp")) return;
			
			e.setDamage(0);
			e.setCancelled(true);
			
			return;
		}
		
	}
	
}
