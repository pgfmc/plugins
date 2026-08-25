package net.pgfmc.survival.gift;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class GiftList extends PlayerCommand {

    public GiftList() {
        super("gifts");
    }

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {

        List<ItemStack> gifts = GiftData.getGifts(pd);
		List<String> list = new ArrayList<>();
		
		if (args.length <= 1) {
			for (int i = 1; i <= (gifts.size() / 10) + 1; i++ ) {
                list.add(String.valueOf(i));
			}
		}

		return list;
	}

    @Override
    public boolean execute(PlayerData pd, String alias, String[] args) {


        List<ItemStack> gifts = GiftData.getGifts(pd);
        int page = 0;

        if (args.length >= 1) {
            page = Integer.valueOf(args[0]) - 1;
        }

        pd.sendMessage(Component.text("-----| GIFTS |-----").color(NamedTextColor.AQUA));

        int iterations = 0;
        for (int i = (10 * page); i < (10 * (page + 1)) && i <= gifts.size() - 1; i++) {
            iterations++;
            pd.sendMessage(Component.text()
                    .content("  " + String.valueOf(i + 1)).color(NamedTextColor.AQUA)
                    .append(Component.text(": ").color(NamedTextColor.GRAY))
                    .append(GiftData.getItemStackName(gifts.get(i)))
                    .build()
                );
        }

        if (iterations == 0) {
            pd.sendMessage(Component.text("  No Gifts Found Here!").color(NamedTextColor.RED));
        }



        
        return true;
    }


}
