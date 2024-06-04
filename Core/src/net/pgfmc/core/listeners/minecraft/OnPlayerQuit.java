package net.pgfmc.core.listeners.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		e.setQuitMessage(null);
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		
		//final String minecraftQuitMessage = ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "]" + ChatColor.RESET + " " + playerdata.getRankedName();
		final String discordQuitMessage = "<:LEAVE:905682349239463957> " + ChatColor.stripColor(playerdata.getRankedName());
		
		PluginMessageType.MESSAGE.send(player, "");
		/*
			.orTimeout(1000L, TimeUnit.MILLISECONDS) // Should only time out if the proxy isn't online
			.whenComplete((result, exception) -> {
				if (exception != null)
				{
					CoreMain.plugin.getServer().broadcastMessage(minecraftQuitMessage);
				}
			});
			*/
		
		PluginMessageType.DISCORD_MESSAGE.send(player, discordQuitMessage);
		
	}

}
