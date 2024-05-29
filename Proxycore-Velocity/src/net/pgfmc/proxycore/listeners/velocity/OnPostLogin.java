package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public class OnPostLogin extends MessageHandler {
	
	@Subscribe
	public void onJoin(PostLoginEvent e)
	{
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		
		GlobalPlayerData.setData(uuid, "username", player.getUsername());
		
		final Component component = Component.text()
				.append(Component.text("[")
						.color(NamedTextColor.GRAY))
				.append(Component.text("+")
						.color(NamedTextColor.GREEN))
				.append(Component.text("] ")
						.color(NamedTextColor.GRAY))
				.append(GlobalPlayerData.getRankedName(uuid))
				.build();
		
		sendToMinecraft(component);
		
		Discord.sendServerMessage("<:JOIN:905023714213625886> " + player.getUsername()).queue();
		
		final String discordUserId = RoleManager.getDiscordUserIdFromPlayerUuid(uuid);
		
		if (discordUserId == null) return;
		
		RoleManager.propogatePlayerRole(player.getUniqueId(), discordUserId);
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}

}
