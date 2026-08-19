package net.pgfmc.hardcore;

import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnAdvancementDoneEvent implements Listener {

    @EventHandler
    public void onAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
        Advancement adv = e.getAdvancement();
        NamespacedKey key = adv.getKey();
        final Player player = e.getPlayer();

        final String yamlPayload = ""; // TODO Get gift from advancement and create YML string
        
        PluginMessageType.GIFT.send(player, yamlPayload, "survival");
         
    }

}
