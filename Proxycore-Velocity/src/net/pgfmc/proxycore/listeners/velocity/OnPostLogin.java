package net.pgfmc.proxycore.listeners.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.roles.PGFRole;
import net.pgfmc.proxycore.roles.RoleManager;

public class OnPostLogin extends MessageHandler {
	
	@Subscribe
	public void onJoin(PostLoginEvent e)
	{
		final Player player = e.getPlayer();
		final PGFRole role = RoleManager.getRoleFromPlayerUuid(player.getUniqueId());
		final Builder textComponentBuilder = Component.text();
		
		textComponentBuilder.append(Component.text("[")
				.color(NamedTextColor.GRAY));
		
		textComponentBuilder.append(Component.text("+")
				.color(NamedTextColor.GREEN));
		
		textComponentBuilder.append(Component.text("] ")
				.color(NamedTextColor.GRAY));
		
		textComponentBuilder.append(Component.text(((role.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + player.getUsername())
				.color(role.getColor()));
		
		sendToMinecraft(textComponentBuilder.build());
		
		Discord.sendServerMessage("<:JOIN:905023714213625886> " + player.getUsername()).queue();
		
		final String discordUserId = RoleManager.getDiscordUserIdFromPlayerUuid(player.getUniqueId());
		
		if (discordUserId == null) return;
		
		RoleManager.propogatePlayerRole(player.getUniqueId(), discordUserId);
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}

}
