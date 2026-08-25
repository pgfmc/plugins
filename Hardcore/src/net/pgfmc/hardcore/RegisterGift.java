package net.pgfmc.hardcore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class RegisterGift extends PlayerCommand {


	public RegisterGift() {
		super("registergift");
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		List<String> list = new ArrayList<>();

        if (pd.getPlayer().getGameMode() != GameMode.CREATIVE) {
            list.add("You can only use this Command in Creative Mode!");
            return list;
        }

        if (args.length == 1) {
            Bukkit.advancementIterator().forEachRemaining(
                    x -> {
                        String name = x.key().asString();
                        if (args[0].startsWith(name)) {
                            list.add(name);
                        }
                    }
                );
		    return list;
        }

        return list;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
        if (pd.getPlayer().getGameMode() != GameMode.CREATIVE) {
            pd.sendMessage(Component.text("You can only use this Command in Creative Mode!").color(NamedTextColor.RED));
            return true;
        }
        NamespacedKey key = NamespacedKey.fromString(args[0]);
        if (key == null || Bukkit.getAdvancement(key) == null) {
            pd.sendMessage(Component.text().content("Make sure to enter a real Advancement Key.").color(NamedTextColor.RED).build());
            return true;
        }

        ItemStack hand = pd.getPlayer().getInventory().getItemInMainHand(); 
        if (hand == null || hand.getType() == Material.AIR) {

            pd.sendMessage(Component.text().content("Make sure there is an item in your hand.").color(NamedTextColor.RED).build());
            return true;
        }
        ItemStack gift = hand.clone();

        Data.setGift(key, gift);
        Data.saveGifts();
        pd.sendMessage(Component.text().content("Set Advancement gift item to currently held item.").color(NamedTextColor.GREEN).build());

        return true;
	}
}
