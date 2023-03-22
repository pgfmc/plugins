package net.pgfmc.parkour.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;
import net.pgfmc.parkour.levels.CharredCaves1;






public class PlayerInteract implements Listener {


    private Region cca1 = new Region(new Vector4(-39, 38, -55, 0), new Vector4(98, 98, 49, 1));




    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        PlayerData pd = PlayerData.from(e.getPlayer());
        Vector4 pos = new Vector4(e.getClickedBlock().getLocation());

        pd.sendMessage(pos.toString());

       if (cca1.contains(pos)) {
           CharredCaves1.script(pos, pd);
       }
    }
}
