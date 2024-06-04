package net.pgfmc.core.listeners.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(null);
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		
		//final String minecraftJoinMessage = ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]" + ChatColor.RESET + " " + playerdata.getRankedName();
		final String discordJoinMessage = "<:JOIN:905023714213625886> " + ChatColor.stripColor(playerdata.getRankedName());
		
		PluginMessageType.MESSAGE.send(player, "");
		/*
			.orTimeout(1000L, TimeUnit.MILLISECONDS) // Should only time out if the proxy isn't online
			.whenComplete((result, exception) -> {
				if (exception != null)
				{
					CoreMain.plugin.getServer().broadcastMessage(minecraftJoinMessage);
				}
			});
			*/
		
		PluginMessageType.DISCORD_MESSAGE.send(player, discordJoinMessage);
		
	}

}
