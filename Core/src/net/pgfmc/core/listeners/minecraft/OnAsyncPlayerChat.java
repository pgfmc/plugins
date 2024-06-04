package net.pgfmc.core.listeners.minecraft;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Profanity;
import net.pgfmc.core.util.proxy.PluginMessageType;


public class OnAsyncPlayerChat implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		e.setCancelled(true);
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		final String message = e.getMessage();
		
		if (Profanity.hasProfanity(message))
		{
			player.sendMessage(ChatColor.RED + "Please do not use blacklisted words!");
			
			/*
			EmbedBuilder eb = Discord.simpleServerEmbed(player.getName(), "https://crafatar.com/avatars/" + player.getUniqueId(), Colors.RED);
			eb.setTitle("Blacklisted word detected! (Minecraft)");
			eb.setDescription("A blacklisted word was detected by " + player.getName() + " in Minecraft.");
			eb.addField("User", player.getName(), false);
			eb.addField("Message", "|| " + handler.getMessage() + " ||", false);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build()).queue();
			*/
			
			return;
		}
		
		final String chatMessage = playerdata.getRankedName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + message;
		
		PluginMessageType.MESSAGE.send(player, chatMessage)
			.orTimeout(1000L, TimeUnit.MILLISECONDS) // Should only time out if the proxy isn't online
			.whenComplete((result, exception) -> {
				if (exception != null)
				{
					CoreMain.plugin.getServer().broadcastMessage(chatMessage);
				}
				
			});
		
		PluginMessageType.DISCORD_MESSAGE.send(player, ChatColor.stripColor(playerdata.getRankedName()) + " -> " + message);
		
	}
	
}
