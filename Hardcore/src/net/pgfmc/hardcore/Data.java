package net.pgfmc.hardcore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.files.Mixins;

public class Data {
    


    private static HashMap<String, ItemStack> gifts = new HashMap<>();
    





    public static void loadGifts() {
		FileConfiguration data = Mixins.getDatabase(Main.plugin.getDataFolder() + File.separator + "advancement_gifts.yml");

        Set<String> keys = data.getKeys(false);

        for (String key : keys) {
            ItemStack item = data.getItemStack(key);
            if (item == null) {
                continue;
            }

            gifts.put(key, item);
        }
    }

    public static void saveGifts() {
        if (gifts == null) return;

		FileConfiguration data = Mixins.getDatabase(Main.plugin.getDataFolder() + File.separator + "advancement_gifts.yml");

        for (String key : gifts.keySet()) {
            data.set(key, gifts.get(key));
        }
        
        try {
            data.save(Main.plugin.getDataFolder() + File.separator + "advancement_gifts.yml");
        } catch (IOException e) {
            Logger.error("Advancement Data Failed to Save!!!!!");
        }
    }

    public static void setGift(NamespacedKey key, ItemStack item) {
        gifts.put(key.asString(), item); 
    }

    public static ItemStack getGift(NamespacedKey key) {
        return gifts.get(key.asString());
    }
}
