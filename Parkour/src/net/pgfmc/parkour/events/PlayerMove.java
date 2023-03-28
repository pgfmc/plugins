package net.pgfmc.parkour.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;

public class PlayerMove implements Listener {




    @EventHandler
    public void playerInteractEvent(PlayerMoveEvent e) {
        PlayerData pd = PlayerData.from(e.getPlayer());
        Vector4 pos = new Vector4(e.getPlayer().getLocation());

        pd.sendMessage(pos.toString());
    }
}
