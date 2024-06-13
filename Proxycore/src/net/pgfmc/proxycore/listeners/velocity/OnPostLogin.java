package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
		new CompletableFuture<Void>()
		.completeOnTimeout(null, 500L, TimeUnit.MILLISECONDS)
		.whenComplete((nullptr, exception) -> {
			Main.plugin.updateTablist();
		});
		
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Component rankedNameComponent = GlobalPlayerData.getRankedName(uuid);
		
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
		
		if (GlobalPlayerData.getData(uuid, "username") == null)
		{
			GlobalPlayerData.setData(uuid, "username", player.getUsername());
		}
		
		RoleManager.updatePlayerRole(uuid);
		
	}

}
