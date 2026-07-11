package net.pgfmc.survival.gift;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.proxy.PluginMessageType;

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
        final List<ItemStack> gifts = getGifts(playerData);
        gifts.add(item);
        playerData.setData(GIFTS, gifts).queue();
        playerData.sendMessage(Component.text()
                .content("You have been sent a gift: ").color(NamedTextColor.AQUA)
                .append(getItemStackName(item))
                .build());
    }



    public static final TextComponent getItemStackName(ItemStack item) {
        return Component.text()
                .append(item.effectiveName())
                .append(Component.text(" x" + String.valueOf(item.getAmount())).color(NamedTextColor.DARK_PURPLE)).build();

    }
    
    private static CompletableFuture<List<List<ItemStack>>> getGiftsFromProxy(Player player) {
    	if (!player.isOnline()) return null;
    	
    	final CompletableFuture<List<List<ItemStack>>> giftsFuture = new CompletableFuture<>();
    	
    	PluginMessageType.GET_GIFTS.send(player).whenComplete((in, exception) -> {
    		if (exception != null) {
    			Logger.error("Exception occurred for plugin message GET_GIFTS:");
    			exception.printStackTrace();
    			return;
    		}
    		
    		in.readUTF();
    		final String giftsSerialized = in.readUTF();
    		final List<List<ItemStack>> gifts = List.of(); // TODO deserialize giftsSerialized
    		
    		giftsFuture.complete(gifts);
	    });
    	
    	return giftsFuture;    	
    }
    
}
