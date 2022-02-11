package net.pgfmc.bot.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		PlayerData pd = PlayerData.from(e.getEntity());
		
		e.setDeathMessage(e.getDeathMessage().replace(pd.getName(), pd.getDisplayName()));
		Discord.sendMessage("<:DEATH:907865162558636072> " + e.getDeathMessage());
		e.setDeathMessage(e.getDeathMessage().replace(pd.getDisplayName(), pd.getRankedName()));
	}

}
