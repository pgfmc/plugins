package net.pgfmc.hardcore;

import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class OnAdvancementDoneEvent implements Listener {

    @EventHandler
    public void onAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
        Advancement adv = e.getAdvancement();
        NamespacedKey key = adv.getKey();




         
    }

}
