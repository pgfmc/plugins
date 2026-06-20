package net.pgfmc.survival.menu.rewards;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.Rewards;
import net.pgfmc.survival.menu.CommandMenuInventory;

public class RewardsListInventory extends ListInventory<String> {
	
	private PlayerData playerdata;

	public RewardsListInventory(PlayerData playerdata) {
		super(27, Component.text("Rewards"));
		
		this.playerdata = playerdata;
		setBack(0, new CommandMenuInventory(playerdata).getInventory());
		
	}

	@Override
	protected List<String> load() {
		// Just a weird way to convert Set<String> to List<String>
		return Rewards.getPlayerRewardsMap(playerdata).keySet().stream().collect(Collectors.toList());
	}

	@Override
	protected Butto toAction(String entry) {
		
		return (player, event) -> {
			
			// This does all the error checking and granting
			Rewards.grantReward(entry, playerdata);
			
			player.openInventory(new RewardsListInventory(playerdata).getInventory());
			
		};
		
	}

	@Override
	protected ItemStack toItem(String entry) {
		return new ItemWrapper(Rewards.getRewardsMap().get(entry))
				.lore(Arrays.asList(Component
						.text("Click to claim.")
						.color(NamedTextColor.GOLD)))
				.item();
	}
	
}
