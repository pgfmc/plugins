package net.pgfmc.survival.masterbook.inv.staff.inv.giverewards.inv;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.inventory.ConfirmInventory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.survival.Rewards;

public class RemoveRewardConfirmInventory extends ConfirmInventory {
	
	private PlayerData playerdata;
	private String reward_id;

	protected RemoveRewardConfirmInventory(final PlayerData playerdata, final String reward_id) {
		super(ChatColor.GRAY + "Remove Reward: " + reward_id, ChatColor.GREEN + "Confirm", ChatColor.RED + "Cancel");
		
		setItem(13, Rewards.getRewardsMap().get(reward_id));
		
		this.playerdata = playerdata;
		this.reward_id = reward_id;
		
	}

	@Override
	protected void confirmAction(Player p, InventoryClickEvent e) {
		Rewards.removeRewardFromList(reward_id);
		playerdata.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
		
	}

	@Override
	protected void cancelAction(Player p, InventoryClickEvent e) {
		playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
		p.openInventory(new GiveRewardsListInventory(playerdata).getInventory());
		
	}

}
