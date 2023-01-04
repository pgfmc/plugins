package net.pgfmc.core.bot.minecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.from(e.getPlayer());
		
		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]" + ChatColor.RESET + " " + pd.getRankedName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getDisplayName()).queue();
	}

}
