package net.pgfmc.survival;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.ItemWrapper;
import net.pgfmc.core.util.files.Mixins;

public class Rewards {
	
	private static Map<String, ItemStack> REWARDS = new HashMap<>();
	private static FileConfiguration REWARDS_YML = Mixins.getDatabase(Main.plugin.getDataFolder() + File.separator + "rewards.yml");
	private static boolean LOADED = false;
	
	public static void loadRewardsFile()
	{
		if (LOADED) return;
		
		
		final Set<String> reward_ids = REWARDS_YML.getKeys(false);
		reward_ids.stream().forEach(reward_id -> REWARDS.put(reward_id, REWARDS_YML.getItemStack(reward_id)));
		
		LOADED = true;
		
	}
	
	public static Map<String, ItemStack> getRewardsMap()
	{
		return new HashMap<>(REWARDS);
	}
	
	/**
	 * Add a reward
	 * 
	 * @param reward The reward
	 * @param reward_id The identifier for this reward
	 */
	public static void addRewardToList(String reward_id, ItemStack reward)
	{
		try {
			REWARDS.put(reward_id, reward.clone());
			REWARDS_YML.set(reward_id, reward);
			REWARDS_YML.save(Main.plugin.getDataFolder() + File.separator + "rewards.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Remove a reward
	 * 
	 * @param reward_id
	 */
	public static void removeRewardFromList(String reward_id)
	{
		try {
			REWARDS.remove(reward_id);
			REWARDS_YML.set(reward_id, null);
			REWARDS_YML.save(Main.plugin.getDataFolder() + File.separator + "rewards.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get the rewards a player has
	 * 
	 * @param playerdata The player that has rewards
	 * @return The rewards map of rewards they can claim
	 */
	public static Map<String, ItemStack> getPlayerRewardsMap(PlayerData playerdata)
	{
		Map<String, ItemStack> playerRewardsMap = new HashMap<>();
		
		getRewardsMap().forEach((reward_id, reward) -> {
			if (!playerdata.hasTag(reward_id))
			{
				playerRewardsMap.put(reward_id, reward.clone());
				
			}			
			
		});;
		
		return playerRewardsMap;
	}
	
	/**
	 * Grant a player a reward if they are eligble
	 * 
	 * @param reward_id The reward's id
	 * @param playerdata The player to have the reward
	 * @return if the grant was successful
	 */
	public static boolean grantReward(String reward_id, PlayerData playerdata)
	{
		if (!playerdata.isOnline()) return false;
		
		final Player player = playerdata.getPlayer();
		final Map<String, ItemStack> rewards = getPlayerRewardsMap(playerdata);
		
		if (!rewards.containsKey(reward_id) && REWARDS.containsKey(reward_id))
		{
			playerdata.sendMessage(ChatColor.RED + "You already claimed this item!");
			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			return false;
		} else if (!rewards.containsKey(reward_id) && !REWARDS.containsKey(reward_id))
		{
			playerdata.sendMessage(ChatColor.RED + "Reward doesn't exist (error)!");
			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			return false;
		}
		
		// Copy of player's inventory
		// Used to check if they have enough room for the reward
		Inventory playerInventoryCopy = Bukkit.createInventory(null, InventoryType.PLAYER);
		playerInventoryCopy.setContents(player.getInventory().getContents());
		
		// addItem() returns a HashMap of ItemStack that couldn't be added
		if (playerInventoryCopy.addItem(rewards.get(reward_id)).size() > 0)
		{
			playerdata.sendMessage(ChatColor.RED + "Could not claim item. Not enough inventory space!");
			playerdata.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			return false;
			
		}
		
		final ItemStack rewardItem = new ItemWrapper(rewards.get(reward_id))
				.l(List.of())
				.gi();
		
		playerdata.addTag(reward_id);
		player.getInventory().addItem(rewardItem);
		
		playerdata.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
		
		return true;		
	}

}
