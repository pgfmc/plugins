package net.pgfmc.survival.masterbook.inv;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.PGFAdvancement;
import net.pgfmc.core.api.inventory.ListInventory;
import net.pgfmc.core.api.inventory.extra.Butto;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.Main;
import net.pgfmc.survival.masterbook.MasterbookInventory;

public class RewardsListInventory extends ListInventory<ItemStack> {
	
	private PlayerData playerdata;

	public RewardsListInventory(PlayerData playerdata) {
		super(27, ChatColor.RESET + "" + "Rewards");
		
		this.playerdata = playerdata;
		setBack(0, new MasterbookInventory(playerdata).getInventory());
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<ItemStack> load() {
		
		final List<ItemStack> rewards = (List<ItemStack>) Optional.ofNullable(playerdata.getData("rewards")).orElse(new ArrayList<ItemStack>());
		
		return rewards;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Butto toAction(ItemStack entry) {
		
		return (player, event) -> {
			// addItem() returns a HashMap of ItemStack that couldn't be added
			if (player.getInventory().addItem(entry).size() > 0)
			{
				playerdata.sendMessage(ChatColor.RED + "Could not claim item. Not enough inventory space!");
				playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				
			} else
			{
				List<ItemStack> rewards = (List<ItemStack>) playerdata.getData("rewards");
				rewards.remove(entry);
				playerdata.setData("rewards", rewards).queue();
				
				playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
				player.openInventory(new RewardsListInventory(playerdata).getInventory());
				
				Bukkit.getScheduler().runTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						// Grants advancement
						PGFAdvancement.REWARDS.grantToPlayer(player);
						
					}});
				
			}
			
		};
		
	}

	@Override
	protected ItemStack toItem(ItemStack entry) {
		return new ItemWrapper(entry).gi();
	}
	
}
