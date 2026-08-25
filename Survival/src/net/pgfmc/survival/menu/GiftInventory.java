package net.pgfmc.survival.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.gift.GiftData;
import net.pgfmc.survival.menu.GiftInventory.GiftEntry;

public class GiftInventory extends ListInventory<GiftEntry> {

    protected static class GiftEntry {
        public ItemStack item;
        public int index;

        public GiftEntry(ItemStack item, int index) {
            this.item = item;
            this.index = index;
        }
    }

    PlayerData playerdata;

    public GiftInventory(PlayerData pd) {
        super(54, Component.text("Claim Gifts!"));

        this.playerdata = pd;
    }



    public List<GiftEntry> load() {
        List<ItemStack> gifts = GiftData.getGifts(playerdata);
        List<GiftEntry> entries = new ArrayList<>();

        int index = 0;
        for (ItemStack item : gifts) {
            entries.add(new GiftEntry(item, index));
            index++;
        }

        return entries;
    }

    public ItemStack toItem(GiftEntry entry) {
        return entry.item;
    }

    public Butto toAction(GiftEntry entry) {
        
        return (p, e) -> {
            Inventory inv = playerdata.getPlayer().getInventory();
            int slot = inv.firstEmpty();

            // -1 for firstEmpty() means no empty slots.
            if (slot == -1) {
                playerdata.sendMessage(Component.text("Make sure you have an empty slot in your inventory!").color(NamedTextColor.RED));
                playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
                return;
            }

            ItemStack item = GiftData.removeGift(playerdata, entry.index);
            playerdata.sendMessage(Component.text()
                    .content("Claimed ").color(NamedTextColor.AQUA)
                    .append(GiftData.getItemStackName(item))
                    .append(Component.text("Claimed ").color(NamedTextColor.AQUA))
                    .build());
            inv.setItem(slot, item);
            p.openInventory(new GiftInventory(playerdata).getInventory());

        };
    }
}
