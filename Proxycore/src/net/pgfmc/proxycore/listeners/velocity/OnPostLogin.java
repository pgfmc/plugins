package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.roles.RoleManager;

public class OnPostLogin {
	
	@Subscribe
	public void onJoin(PostLoginEvent e)
	{
		/*
		new CompletableFuture<Void>()
		.completeOnTimeout(null, 500L, TimeUnit.MILLISECONDS)
		.whenComplete((nullptr, exception) -> {
		});
		*/

		Main.plugin.updateTablist();

		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
        final GlobalPlayerData playerData = GlobalPlayerData.fromUuid(uuid);
        playerData.username = player.getUsername();
		final Component rankedNameComponent = playerData.getRankedName(player);
        playerData.save();
		
		final Component component = Component.text()
				.append(Component.text("[")
						.color(NamedTextColor.GRAY))
				.append(Component.text("+")
						.color(NamedTextColor.GREEN))
				.append(Component.text("] ")
						.color(NamedTextColor.GRAY))
				.append(rankedNameComponent)
				.build();
		
		final String rankedNameNoColor = PlainTextComponentSerializer.plainText().serialize(rankedNameComponent);
		
		MessageHandler.sendToMinecraft(component);
		MessageHandler.sendToDiscord("<:JOIN:905023714213625886> " + rankedNameNoColor);

        playerData.username = player.getUsername();
        playerData.save();
		
		final String discordUserId = RoleManager.getDiscordUserIdFromPlayerUuid(uuid);
		
		if (discordUserId == null || discordUserId.isBlank()) return;
		
		RoleManager.updatePlayerRole(uuid);
		
	}

}
