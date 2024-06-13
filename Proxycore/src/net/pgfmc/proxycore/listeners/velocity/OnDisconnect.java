package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public class OnDisconnect {
	
	@Subscribe
	public void onPlayerDisconnect(DisconnectEvent e)
	{
		Main.plugin.updateTablist();
		
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		
        final GlobalPlayerData playerData = GlobalPlayerData.fromUuid(uuid);
		final Component rankedNameComponent = playerData.getRankedName(player);
		
		final Component component = Component.text()
				.append(Component.text("[")
						.color(NamedTextColor.GRAY))
				.append(Component.text("-")
						.color(NamedTextColor.RED))
				.append(Component.text("] ")
						.color(NamedTextColor.GRAY))
				.append(rankedNameComponent)
				.build();
		
		final String rankedNameNoColor = PlainTextComponentSerializer.plainText().serialize(rankedNameComponent);
		
		MessageHandler.sendToMinecraft(component);
		MessageHandler.sendToDiscord("<:LEAVE:905682349239463957> " + rankedNameNoColor);
		
	}
	
}
