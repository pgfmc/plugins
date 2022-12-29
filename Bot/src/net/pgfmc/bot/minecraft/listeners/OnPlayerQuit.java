package net.pgfmc.bot.minecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.bot.discord.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerQuit implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "]" + ChatColor.RESET + " " + pd.getRankedName());
		Discord.sendMessage("<:LEAVE:905682349239463957> " + pd.getDisplayName()).queue();
	}
}
