package net.pgfmc.survival.menu.rewards;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.Reward;
import net.pgfmc.survival.menu.CommandMenuInventory;

public class RewardsListInventory extends ListInventory<Reward> {
	
	private PlayerData playerdata;

	public RewardsListInventory(PlayerData playerdata) {
		super(27, "Rewards");
		
		this.playerdata = playerdata;

		setBack(0, new CommandMenuInventory(playerdata).getInventory());
	}

	@Override
	protected List<Reward> load() {
		return Reward.getPlayerRewards(this.playerdata);
	}

	@Override
	protected Butto toAction(Reward entry) {
		
		return (player, event) -> {
			
			// This does all the error checking and granting
			entry.grant(this.playerdata);
			
			player.openInventory(new RewardsListInventory(playerdata).getInventory());
			
		};
	}

	@Override
	protected ItemStack toItem(Reward entry) {
		final List<String> lore = List.of(entry.getDescription(), "(Timestamp) " + String.valueOf(entry.getTimestamp()), "(Expiration) " + String.valueOf(entry.getExpiration()), "(UUID) " + entry.getUUID());
        lore.addAll(entry.getItems().stream().map(item -> item.getType().toString()).toList());

        return new ItemWrapper(entry.getGiftBox())
            .n(entry.getTitle())
            .l(lore)
            .gi();
	}
	
}
