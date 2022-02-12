package net.pgfmc.bot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerQuit implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		if (pd.hasTag("fake-leave"))
		{
			pd.removeTag("fake-leave");
			e.setQuitMessage("");
			return;
		}
		
		e.setQuitMessage("§7[§c-§7]§r " + pd.getRankedName());
		Discord.sendMessage("<:LEAVE:905682349239463957> " + pd.getDisplayName()).queue();
	}
}
