package net.pgfmc.claims.ownable.entities;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.claims.ownable.block.table.ClaimsLogic.Range;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.util.vector4.Vector4;

public class PlayerDamagedEvent implements Listener {

    @EventHandler
    public void playerDamagedEvent(EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Vector4 position = new Vector4(e.getEntity().getLocation());
        Claim c = ClaimsTable.getClosestClaim(position, Range.PROTECTED);
        if (c.getPlayer() == null) {
            e.setDamage(0);
        }
    }


}
