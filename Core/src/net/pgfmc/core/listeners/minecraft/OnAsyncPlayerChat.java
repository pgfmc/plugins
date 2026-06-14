package net.pgfmc.core.listeners.minecraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Profanity;
import net.pgfmc.core.util.proxy.PluginMessageType;


public class OnAsyncPlayerChat implements Listener {
	
	@EventHandler
	public void onChat(AsyncChatEvent e)
	{
		e.setCancelled(true);
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		final Component message = e.message();
		
		if (Profanity.hasProfanity(Component.textOfChildren(message).content()))
		{
			player.sendMessage(NamedTextColor.RED + "Please do not use blacklisted words!");
			
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
		
        final Component chatMessage = Component.text().append(playerdata.getRankedName()).append(Component.text(" -> ", NamedTextColor.GRAY)).append(message.color(NamedTextColor.WHITE)).build();

		PluginMessageType.MESSAGE.send(player, chatMessage);
		PluginMessageType.DISCORD_MESSAGE.send(player, playerdata.getDisplayName() + " -> " + message);
	}
}
