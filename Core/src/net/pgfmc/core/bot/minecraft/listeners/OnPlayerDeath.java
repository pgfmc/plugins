package net.pgfmc.core.bot.minecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		PlayerData pd = PlayerData.from(e.getEntity());
		
		e.setDeathMessage(e.getDeathMessage().replace(pd.getName(), ChatColor.stripColor(pd.getRankedName())));
		Discord.sendMessage("<:DEATH:907865162558636072> " + e.getDeathMessage()).queue();
		e.setDeathMessage(e.getDeathMessage().replace(ChatColor.stripColor(pd.getRankedName()), pd.getRankedName()));
		
	}

}
