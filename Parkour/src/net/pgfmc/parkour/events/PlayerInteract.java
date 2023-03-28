package net.pgfmc.parkour.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;
import net.pgfmc.parkour.levels.RuinedCaverns;

public class PlayerInteract implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        PlayerData pd = PlayerData.from(e.getPlayer());
        Vector4 pos = new Vector4(e.getClickedBlock().getLocation());

        pd.sendMessage(pos.toString());

        if (RuinedCaverns.act1.contains(pos)) {
            RuinedCaverns.interact(pos, pd);
        }

    }
}
