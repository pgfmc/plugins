package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public class OnDisconnect extends MessageHandler {
	
	@Subscribe
	public void onPlayerDisconnect(DisconnectEvent e)
	{
		Main.plugin.updateTablist();
		
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Component displayNameComponent = GlobalPlayerData.getRankedName(uuid);
		final String displayName = PlainTextComponentSerializer.plainText().serialize(displayNameComponent);
		
		final Component component = Component.text()
				.append(Component.text("[")
						.color(NamedTextColor.GRAY))
				.append(Component.text("-")
						.color(NamedTextColor.RED))
				.append(Component.text("] ")
						.color(NamedTextColor.GRAY))
				.append(displayNameComponent)
				.build();
		
		sendToMinecraft(component);
		
		Discord.sendServerMessage("<:LEAVE:905682349239463957> " + displayName).queue();
		
		Main.plugin.updateTablist();
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}
	
}
