package net.pgfmc.survival.cmd.pvp;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import net.pgfmc.core.api.playerdata.PlayerData;

public class PvpEvent implements Listener {
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
        PlayerData attacker = null;
        PlayerData target = null;

        if (e.getDamager() instanceof OfflinePlayer) {
            attacker = PlayerData.from((OfflinePlayer) e.getDamager());

        } else if (e.getDamager() instanceof Tameable) {
            Tameable pet = (Tameable) e.getDamager();
            if (!(pet.getOwner() instanceof OfflinePlayer)) {return;}
            attacker = PlayerData.from((OfflinePlayer) pet.getOwner());

        } else if (e.getDamager() instanceof Projectile) {
            Projectile bullet = (Projectile) e.getDamager();
            if (!(bullet.getShooter() instanceof OfflinePlayer)) {return;} 
            attacker = PlayerData.from((OfflinePlayer) bullet.getShooter());

        } else {return;}

        if (e.getEntity() instanceof OfflinePlayer) {
            target = PlayerData.from((OfflinePlayer) e.getEntity());

        } else if (e.getEntity() instanceof Tameable) {
            Tameable pet = (Tameable) e.getEntity();
            if (!(pet.getOwner() instanceof OfflinePlayer)) {return;}
            target = PlayerData.from((OfflinePlayer) pet.getOwner());
        } else { return;}

        if (attacker == null || target == null) {return;}

		if (attacker.hasTag("pvp") && target.hasTag("pvp")) return;
		
        e.setDamage(0.0);
		e.setCancelled(true);
	}

    @EventHandler
    public void onEntityTargetEntity(EntityTargetEvent e) {

        if (!(e.getEntity() instanceof Tameable)) {return;}
        Tameable pet = (Tameable) e.getEntity();

        if (!(pet.getOwner() instanceof OfflinePlayer)) {return;}
        PlayerData owner = PlayerData.from((OfflinePlayer) pet.getOwner());

        if (e.getTarget() == null) {return;}
        if (!(e.getTarget() instanceof OfflinePlayer)) {return;}
        PlayerData target = PlayerData.from((OfflinePlayer) e.getTarget());

        if (target.hasTag("pvp") && owner.hasTag("pvp")) {return;}
        e.setCancelled(true);
    }
}
