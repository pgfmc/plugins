package net.pgfmc.survival.gift;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;

// Easy Methods for using gifts
public final class GiftData {

    private static final String GIFTS = "gifts";

    
    public static final ArrayList<ItemStack> getGifts(PlayerData playerData) {

        ArrayList<ItemStack> gifts = playerData.getData(GIFTS);
        if (gifts == null) {
            gifts = new ArrayList<>();
            playerData.setData(GIFTS, gifts);
        }
        return gifts;
    }

    public static final void giveGift(PlayerData playerData, ItemStack item) {
        getGifts(playerData).add(item);
    }
    





}
