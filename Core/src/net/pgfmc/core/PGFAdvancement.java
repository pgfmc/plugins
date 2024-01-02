package net.pgfmc.core;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

/**
 * These are a subset of advancements from the datapack
 * that can only be awarded through cheats/commands
 */
public enum PGFAdvancement {
	
	CALL_ME_TRACER,
	COLONIZATION,
	CRAFTING_TABLE_ON_DEMAND,
	DINNERS_READY,
	ENDER_CHEST_ON_DEMAND,
	ENTERING_COMBAT,
	EXPERT_EXECUTOR,
	HOME_IS_WHERE_THE_BASE_IS,
	IMPERSONATOR,
	MAKE_FRIENDS,
	RELOCATING,
	REWARDS,
	SOCIAL_SYNC,
	THANK_YOU,
	THEYRE_MY_VACATION_HOMES,
	TOWN_CENTER,
	TP_PLEASE;
	
	/**
	 * Grants the related advancement to the player
	 * @param player An online player
	 * @return If the advancement was granted
	 */
	public void grantToPlayer(Player player)
	{
		Bukkit.getScheduler().runTask(CoreMain.plugin, new Runnable() {
			@Override
			public void run() {
				// Online players only
				if (player == null || !player.isOnline()) return;
				
				// The location of the advancement in the datapack
				// e.g. pgfmc:social_sync
				// 'pgfmc:' is the namespace
				// 'social_sync' is the name of the advancement
				final NamespacedKey key = NamespacedKey.fromString("pgfmc:" + toString());
				// The advancement itself
				final Advancement advancement = Bukkit.getAdvancement(key);
				// Contains information about if criteria are completed
				final AdvancementProgress progress = player.getAdvancementProgress(advancement);
				
				
				// Force complete each criteria
				// Once all criteria is complete/awarded, the advancement will be granted automatically by the game
				for (final String criteria : advancement.getCriteria())
				{
					progress.awardCriteria(criteria);
				}
				
			}});
		
	}
	
	// The enum names match the advancement names
	// Namespace keys are always lowercase
	@Override
	public String toString()
	{
		return name().toLowerCase();
	}
	

}
