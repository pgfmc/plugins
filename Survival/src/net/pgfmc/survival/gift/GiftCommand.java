package net.pgfmc.survival.gift;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
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
		}

		return list;
	}

    @Override
    public boolean execute(PlayerData pd, String alias, String[] args) {
        if (args.length != 1) {
            return false;
        }

        PlayerData giftee = null;

        for (PlayerData playerData : PlayerData.getPlayerDataSet()) {
            if (args[0].equals(playerData.getName())) {
                giftee = playerData; 
            }
        }

        if (giftee == null) {
			pd.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        ItemStack hand = pd.getPlayer().getInventory().getItemInMainHand(); 
        if (hand == null || hand.getType() == Material.AIR) {
            pd.sendMessage(Component.text().content("Make sure there is an item in your hand.").color(NamedTextColor.RED).build());
            return true;
        }
        ItemStack gift = hand.clone();
        
        if (pd.getPlayer().getGameMode() != GameMode.CREATIVE) {
            pd.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }

        pd.sendMessage(ChatColor.GREEN + "Sent Player " + ChatColor.RESET + gift.effectiveName() + " " + ChatColor.DARK_PURPLE + "x" + String.valueOf(gift.getAmount()));
        GiftData.giveGift(giftee, gift);
        
        return true;
    }

}
