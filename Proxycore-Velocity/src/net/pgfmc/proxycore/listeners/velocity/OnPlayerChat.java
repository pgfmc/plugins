package net.pgfmc.proxycore.listeners.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.roles.PGFRole;
import net.pgfmc.proxycore.roles.RoleManager;

public final class OnPlayerChat extends MessageHandler {
	
	@Subscribe
	public void onChat(PlayerChatEvent e) {
		final Player player = e.getPlayer();
		final String message = e.getMessage();
		
		sendToDiscord(player.getUsername(), Discord.convertDiscordMentions(message));
		
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
		
		final PGFRole role = RoleManager.getRoleFromPlayerUuid(player.getUniqueId());
		final Builder textComponentBuilder = Component.text();
		
		textComponentBuilder.append(Component.text(((role.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + player.getUsername())
				.color(role.getColor()));
		
		textComponentBuilder.append(Component.text(" -> ")
				.color(NamedTextColor.DARK_GRAY));
		
		textComponentBuilder.append(Component.text(message)
				.color(getExpectedTextColor(player.getUsername())));
				
		sendToMinecraft(textComponentBuilder.build());
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}
	
}
