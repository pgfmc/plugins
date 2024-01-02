package net.pgfmc.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import net.pgfmc.core.api.playerdata.PlayerData;

public class Rewards {
	
	/**
	 * Give a claimable reward to all players
	 * @param reward The reward to be given
	 */
	public static void giveRewardToAllPlayers(ItemStack reward)
	{
		for (final PlayerData playerdata : PlayerData.getPlayerDataSet())
		{
			List<ItemStack> rewards = getPlayerRewards(playerdata);
			rewards.add(reward);
			
			playerdata.setData("rewards", rewards).queue();
			
		}
		
	}
	
	/**
	 * Give a claimable reward to a player
	 * @param playerdata The player to give the award to. Can be offline
	 * @param reward The reward to be given
	 */
	public static void giveRewardToPlayer(PlayerData playerdata, ItemStack reward)
	{
		List<ItemStack> rewards = getPlayerRewards(playerdata);
		rewards.add(reward);
		
		playerdata.setData("rewards", rewards).queue();
	}
	
	/**
	 * Represents the rewards a player has
	 * @param playerdata The player to get the rewards from
	 * @return A list of rewards
	 */
	@SuppressWarnings("unchecked")
	public static List<ItemStack> getPlayerRewards(PlayerData playerdata)
	{
		List<ItemStack> rewards = (List<ItemStack>) Optional.ofNullable(playerdata.getData("rewards")).orElse(new ArrayList<ItemStack>());
		
		return rewards;
	}

}
