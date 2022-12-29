package net.pgfmc.bot.minecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.bot.discord.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]" + ChatColor.RESET + " " + pd.getRankedName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getDisplayName()).queue();
	}

}
