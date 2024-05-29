package net.pgfmc.core.bot.minecraft.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		PlayerData playerdata = PlayerData.from(e.getEntity());
		
		e.setDeathMessage(e.getDeathMessage().replace(ChatColor.stripColor(playerdata.getRankedName()), playerdata.getRankedName()));
		
		final String message = new String("<:DEATH:907865162558636072> " + e.getDeathMessage())
				.replace(playerdata.getName(), ChatColor.stripColor(playerdata.getRankedName()));
		
		PluginMessageType.DISCORD_MESSAGE.send(playerdata.getPlayer(), message);
		
	}

}
