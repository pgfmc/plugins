package net.pgfmc.survival.gift;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;

// Easy Methods for using gifts
public final class GiftData {

    public static final String GIFTS = "gifts";
    
    public static final List<ItemStack> getGifts(PlayerData playerData) {

        ArrayList<ItemStack> gifts = playerData.getData(GIFTS);
        if (gifts == null) {
            gifts = new ArrayList<>();
            playerData.setData(GIFTS, gifts).queue();
        }
        return Collections.unmodifiableList(gifts);
    }

    public static final void giveGift(PlayerData playerData, ItemStack item) {
        final List<ItemStack> gifts = playerData.getData(GIFTS);
        gifts.add(item);
        playerData.setData(GIFTS, gifts).queue();
        playerData.sendMessage(Component.text()
                .content("You have been sent a gift: ").color(NamedTextColor.AQUA)
                .append(getItemStackName(item))
                .build());
    }

    public static final ItemStack removeGift(PlayerData playerdata, int index) {
        ArrayList<ItemStack> gifts = playerdata.getData(GIFTS);
        if (gifts.size() <= index) return null;
        ItemStack removed = gifts.remove(index);
        playerdata.setData(GIFTS, gifts).queue();
        return removed;
    }



    public static final TextComponent getItemStackName(ItemStack item) {
        return Component.text()
                .append(item.effectiveName())
                .append(Component.text(" x" + String.valueOf(item.getAmount())).color(NamedTextColor.DARK_PURPLE)).build();

    }
}
