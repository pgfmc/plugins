package net.pgfmc.parkour.events;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;
import net.pgfmc.parkour.levels.Level;

public class PlayerMove implements Listener {


    private static HashMap<PlayerData, Vector4> cache = new HashMap<PlayerData, Vector4>();


    @EventHandler
    public void playerInteractEvent(PlayerMoveEvent e) {
        PlayerData pd = PlayerData.from(e.getPlayer());
        Vector4 pos = new Vector4(e.getTo());



        if (cache.containsKey(pd) && cache.get(pd).equals(pos)) {
            return;
        }
        pd.sendMessage(pos.toString());

        for (Level level : Level.levels) {
            if (level.contains(pos)) {
                level.move(pos, pd);
                cache.put(pd, pos);
                return;
            }
        }
    }
}
