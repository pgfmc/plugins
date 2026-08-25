package net.pgfmc.survival.gift;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class GiftClaim extends PlayerCommand {

    public GiftClaim() {
        super("claimgift");
    }

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {

        List<ItemStack> gifts = GiftData.getGifts(pd);

        
		List<String> list = new ArrayList<>();
		
		if (args.length <= 1) {
			for (int i = 1; i <= gifts.size(); i++ ) {
                String value = String.valueOf(i);
                if (args[0].startsWith(value)) {
                    list.add(String.valueOf(i));
                }
			}

            if (args[0].startsWith("pack")) {
                list.add("pack");
            }
		}

		return list;
	}

    @Override
    public boolean execute(PlayerData pd, String alias, String[] args) {

        if (args.length < 1) {
            return false;
        }



        Inventory inv = pd.getPlayer().getInventory();
        int slot = inv.firstEmpty();

        // -1 for firstEmpty() means no empty slots.
        if (slot == -1) {
            pd.sendMessage(Component.text("Make sure you have an empty slot in your inventory!").color(NamedTextColor.RED));
            return true;
        }

        ItemStack itemGift = null;

        if (args[0].equals("pack")) {
            itemGift = GiftData.removePack(pd);

            if (itemGift == null) {
                pd.sendMessage(Component.text("You need 27 items to get a pack!").color(NamedTextColor.RED));
                return true;
            }

            pd.sendMessage(Component.text()
                    .content("Claimed 27 gifts").color(NamedTextColor.AQUA)
                    .build());
        } else {
            int gift = -1;
            try {
                gift = Integer.valueOf(args[0]);
            } catch (Exception e) {
                pd.sendMessage(Component.text("Put in a number.").color(NamedTextColor.RED));
                return true;
            }

            itemGift = GiftData.removeGift(pd, gift -1);
            if (itemGift ==  null) {
                pd.sendMessage(Component.text("Number is more than the amount of gifts you have.").color(NamedTextColor.RED));
                return true;
            }

            pd.sendMessage(Component.text()
                    .content("Claimed ").color(NamedTextColor.AQUA)
                    .append(GiftData.getItemStackName(itemGift))
                    .append(Component.text("Claimed ").color(NamedTextColor.AQUA))
                    .build());
        }

        inv.setItem(slot, itemGift);
        return true;
    }
}