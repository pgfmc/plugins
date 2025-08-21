package net.pgfmc.core.listeners.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(null);
		
		/*
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		final String discordJoinMessage = "<:JOIN:905023714213625886> " + ChatColor.stripColor(playerdata.getRankedName());
		
		PluginMessageType.DISCORD_MESSAGE.send(player, discordJoinMessage);
		*/
		
	}

}
