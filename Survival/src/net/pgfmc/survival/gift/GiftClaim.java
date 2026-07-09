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

        ArrayList<ItemStack> gifts = GiftData.getGifts(pd);

        
		List<String> list = new ArrayList<>();
		
		if (args.length <= 1) {
			for (int i = 1; i <= gifts.size(); i++ ) {
                list.add(String.valueOf(i));
			}
		}

		return list;
	}

    @Override
    public boolean execute(PlayerData pd, String alias, String[] args) {

        if (args.length < 1) {
            return false;
        }

        int gift = Integer.valueOf(args[0]);
        ArrayList<ItemStack> giftList = GiftData.getGifts(pd);

        if (gift > giftList.size()) {
            pd.sendMessage(Component.text("Index is out of Bounds.").color(NamedTextColor.RED));
            return true;
        }

        Inventory inv = pd.getPlayer().getInventory();
        int slot = inv.firstEmpty();

        // -1 for firstEmpty() means no empty slots.
        if (slot == -1) {
            pd.sendMessage(Component.text("Make sure you have an empty slot in your inventory!").color(NamedTextColor.RED));
            return true;
        }

        ItemStack item = GiftData.getGifts(pd).remove(gift -1);
        pd.sendMessage(Component.text()
                .content("Claimed ").color(NamedTextColor.AQUA)
                .append(GiftData.getItemStackName(item))
                .append(Component.text("Claimed ").color(NamedTextColor.AQUA))
                .build());
        inv.setItem(slot, item);
        
        return true;
    }

}
