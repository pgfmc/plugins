package net.pgfmc.core.bot.minecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;

public class OnPlayerAdvancementDone implements Listener {
	
	@EventHandler
	public void advancementDone(PlayerAdvancementDoneEvent e)
	{
		if (e.getAdvancement().getDisplay() == null) return;
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		final Advancement advancement = e.getAdvancement();
		String advancementAnnouncement = "";
		
		switch (advancement.getDisplay().getType())
		{
			case TASK:
				advancementAnnouncement = "has made the advancement [" + advancement.getDisplay().getTitle() + "]!";
				break;
			case GOAL:
				advancementAnnouncement = "has reached the goal [" + advancement.getDisplay().getTitle() + "]!";
				break;
			case CHALLENGE:
				advancementAnnouncement = "has completed the challenge [" + advancement.getDisplay().getTitle() + "]!";
				break;
		}
		
		Discord.sendMessage("<:dwarf:1191762269261017119> " + ChatColor.stripColor(playerdata.getRankedName()) + " " + advancementAnnouncement).queue();
		
	}
	
}
