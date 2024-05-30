package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public final class OnPlayerChat extends MessageHandler {
	
	@Subscribe
	public void onChat(PlayerChatEvent e) {
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Component displayNameComponent = GlobalPlayerData.getRankedName(uuid);
		final String displayName = PlainTextComponentSerializer.plainText().serialize(displayNameComponent);
		final String message = e.getMessage();
		
		sendToDiscord(displayName, Discord.convertDiscordMentions(message));
		
		/*
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
		*/
		
		final Component component = Component.text()
				.append(displayNameComponent)
				.append(Component.text(" -> ")
						.color(NamedTextColor.DARK_GRAY))
				.append(Component.text(message)
						.color(getExpectedTextColor(player.getUsername())))
				.build();
				
		sendToMinecraft(component);
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}
	
}
