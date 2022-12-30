package net.pgfmc.bot.minecraft.listeners;

import java.time.OffsetDateTime;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.util.Colors;
import net.pgfmc.bot.util.MessageHandler;
import net.pgfmc.core.chat.Profanity;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Makes all the chat colorful :)
 * @author bk (basically CrimsonDart now)
 *
 */
public class OnAsyncPlayerChat implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e)
	{
		if (e.isCancelled()) return;
		
		final Player player = e.getPlayer();
		final PlayerData pd = PlayerData.from(player);
		
		final MessageHandler handler = new MessageHandler(e.getMessage(), player);
		
		if (handler.getMessage().length() > 95)
		{
			player.sendMessage(ChatColor.RED + "Your message is too long (max 95 characters).");
			handler.setMessage(handler.getMessage().substring(0, 95) + "(...)");
		}
		
		if (Profanity.hasProfanity(handler.getMessage()))
		{
			player.sendMessage(ChatColor.RED + "Please do not use blacklisted words!");
			e.setCancelled(true);
			
			EmbedBuilder eb = Discord.simpleServerEmbed(player.getName(), "https://crafatar.com/avatars/" + player.getUniqueId(), Colors.RED);
			eb.setTitle("Blacklisted word detected! (Minecraft)");
			eb.setDescription("A blacklisted word was detected by " + player.getName() + " in Minecraft.");
			eb.addField("User", player.getName(), false);
			eb.addField("Message", "|| " + handler.getMessage() + " ||", false);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build()).queue();
			
			return;
		}
		
		handler.setMessage(Discord.getMessageWithDiscordMentions(handler.getMessage()));
		
		e.setFormat(pd.getRankedName() + ChatColor.DARK_GRAY + " -> " + MessageHandler.getTrackColor(player.getUniqueId().toString()) + "%2$s"); // %2$s means 2nd argument (the chat message), %1$s would be the player's display name
		
		handler.send();
		
	}
	
}
