package net.pgfmc.parkour.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;
import net.pgfmc.parkour.levels.Level;

public class PlayerInteract implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        PlayerData pd = PlayerData.from(e.getPlayer());
        Vector4 pos = new Vector4(e.getClickedBlock().getLocation());

        pd.sendMessage(pos.toString());

        for (Level level : Level.levels) {
            if (level.contains(pos)) {
                level.interact(pos, pd);
                return;
            }
        }
    }
}
