package net.pgfmc.survival.menu.staff.giverewards;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.survival.Rewards;

public class RemoveRewardConfirmInventory extends ConfirmInventory {
	
	private PlayerData playerdata;
	private String reward_id;

	protected RemoveRewardConfirmInventory(final PlayerData playerdata, final String reward_id) {
		super("Remove Reward", ChatColor.GREEN + "Confirm", ChatColor.RED + "Cancel");
		
		setItem(13, new ItemWrapper(Rewards.getRewardsMap().get(reward_id)).l(ChatColor.GRAY + reward_id).gi());
		
		this.playerdata = playerdata;
		this.reward_id = reward_id;
		
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		Rewards.removeRewardFromList(reward_id);
		playerdata.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
		p.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
		p.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
		
	}

}
