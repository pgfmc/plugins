package net.pgfmc.core.listeners.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerAdvancementDone implements Listener {
	
	@EventHandler
	public void advancementDone(PlayerAdvancementDoneEvent e)
	{
		if (e.getAdvancement().getDisplay() == null) return;
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		final Advancement advancement = e.getAdvancement();
		String advancementMessage = "";
		
		switch (advancement.getDisplay().getType())
		{
			case GOAL:
				advancementMessage = "has reached the goal [" + advancement.getDisplay().getTitle() + "]!";
				break;
			case CHALLENGE:
				advancementMessage = "has completed the challenge [" + advancement.getDisplay().getTitle() + "]!";
				break;
			case TASK:
			default:
				advancementMessage = "has made the advancement [" + advancement.getDisplay().getTitle() + "]!";
				break;
		}
		
		/*
		 * No easy way to cancel/set the advancement message
		 * 
		final String finalAdvancementMessage = advancementMessage;
		
		PluginMessageType.MESSAGE.send(player, playerdata.getRankedName() + " " + advancementMessage)
		.orTimeout(1000L, TimeUnit.MILLISECONDS) // Should only time out if the proxy isn't online
		.whenComplete((result, exception) -> {
			if (exception != null)
			{
				CoreMain.plugin.getServer().broadcastMessage(playerdata.getRankedName() + " " + finalAdvancementMessage); // requires final or effectively final
			}
		});
		*/
		
		PluginMessageType.DISCORD_MESSAGE.send(player, "<:dwarf:1191762269261017119> " + ChatColor.stripColor(playerdata.getRankedName()) + " " + advancementMessage);
		
	}
	
}
