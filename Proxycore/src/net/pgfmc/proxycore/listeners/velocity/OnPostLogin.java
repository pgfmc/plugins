package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public class OnPostLogin extends MessageHandler {
	
	@Subscribe
	public void onJoin(PostLoginEvent e)
	{
		new CompletableFuture<Void>()
		.completeOnTimeout(null, 500L, TimeUnit.MILLISECONDS)
		.whenComplete((nullptr, exception) -> {
			Main.plugin.updateTablist();
		});
		
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Component displayNameComponent = GlobalPlayerData.getRankedName(uuid);
		final String displayName = PlainTextComponentSerializer.plainText().serialize(displayNameComponent);
		
		GlobalPlayerData.setData(uuid, "username", player.getUsername());
		
		final Component component = Component.text()
				.append(Component.text("[")
						.color(NamedTextColor.GRAY))
				.append(Component.text("+")
						.color(NamedTextColor.GREEN))
				.append(Component.text("] ")
						.color(NamedTextColor.GRAY))
				.append(displayNameComponent)
				.build();
		
		sendToMinecraft(component);
		
		Discord.sendServerMessage("<:JOIN:905023714213625886> " + displayName).queue();
		
		final String discordUserId = RoleManager.getDiscordUserIdFromPlayerUuid(uuid);
		
		if (discordUserId == null) return;
		
		RoleManager.propogatePlayerRole(uuid);
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}

}
