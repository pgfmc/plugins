package net.pgfmc.hardcore;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnAdvancementDoneEvent implements Listener {

    @EventHandler
    public void onAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
        Advancement adv = e.getAdvancement();
        NamespacedKey key = adv.getKey();
        final Player player = e.getPlayer();

        // TODO Get gift from advancement
        final List<Object> giftItems = Arrays.asList(new ItemStack(Material.DIAMOND));
        final ItemStack giftItem = new ItemStack(Material.DIAMOND);
        
        PluginMessageType.SEND_GIFT.sendList(player, giftItems); // To send a list of gift items
        PluginMessageType.SEND_GIFT.send(player, giftItem); // To send just one gift item
        PluginMessageType.SEND_GIFT.send(player, giftItem, giftItem, giftItem); // Another way to send a list of gift items
         
    }

}
