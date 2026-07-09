package net.pgfmc.survival.gift;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class GiftCommand extends PlayerCommand {

    public GiftCommand() {
        super("gift");
    }

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {

        
		List<String> list = new ArrayList<>();
		
		if (args.length == 1) {
			for (PlayerData playerdata : PlayerData.getPlayerDataSet()) {
                if (pd == playerdata && pd.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    continue; 
                }

				if (playerdata.getName().startsWith(args[0])) {
					list.add(pd.getName());
				}
			}
		} else if (args.length == 2) {
            if ("held_item".startsWith(args[1])) {
                list.add("held_item");
            }
            for (Material mat : Material.values()) {
                if (mat.isItem()) {
                    String matName = mat.toString();
                    if (matName.startsWith(args[1])) {
                        list.add(matName);
                    }
                }
            }
        } else if (args.length == 3 && !(args[2].equals("held_item"))) {
            list.add("[amount]");
        }

		return list;
	}

    @Override
    public boolean execute(PlayerData pd, String alias, String[] args) {

        if (args.length < 2) {
            return false;
        }

        PlayerData giftee = null;

        for (PlayerData playerData : PlayerData.getPlayerDataSet()) {
            if (args[0].equals(playerData.getName())) {
                giftee = playerData; 
            }
        }

        if (giftee == null) {
            pd.sendMessage(Component.text().content("Player not found").color(NamedTextColor.RED).build());
            return true;
        }

        ItemStack gift = null;

        if (args[1].equals("held_item")) {
            ItemStack hand = pd.getPlayer().getInventory().getItemInMainHand(); 
            if (hand == null || hand.getType() == Material.AIR) {

                pd.sendMessage(Component.text().content("Make sure there is an item in your hand before using 'held_item'.").color(NamedTextColor.RED).build());
                return true;
            }
            gift = hand.clone();
        } else {
            Material mat = Material.getMaterial(args[1]);
            if (mat == null || mat == Material.AIR) {
                pd.sendMessage(Component.text().content("Item not found.").color(NamedTextColor.RED).build());
                return true;
            } 

            int amount = 1;

            if (args.length >= 3) {
                amount = Integer.valueOf(args[2]).intValue();
                if (amount < 1 || amount > mat.getMaxStackSize()) {
                    pd.sendMessage(Component.text()
                            .content("Amount invalid! Use an amount between 1-." + String.valueOf(mat.getMaxStackSize()))
                            .color(NamedTextColor.RED).build());
                    return true;
                }
            }
            gift = mat.asItemType().createItemStack(amount);
        }

        if (gift == null) {
            return false;
        }

        pd.sendMessage(Component.text()
                .content("Sent Player ").color(NamedTextColor.GREEN)
                .append(GiftData.getItemStackName(gift))
                .build()
            );
        GiftData.giveGift(giftee, gift);
        
        return true;
    }

}
