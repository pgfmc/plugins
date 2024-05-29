package net.pgfmc.proxycore.bot.util;

import java.util.Collection;
import java.util.Objects;

import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;

public abstract class MessageHandler extends ListenerAdapter {
	
	private String lastSender;
	private NamedTextColor currentTextColor = NamedTextColor.WHITE;
	
	@Override
	public abstract void onMessageReceived(MessageReceivedEvent e);
	
	public void sendToMinecraft(final Component message)
	{
		final Collection<Player> allPlayers = Main.plugin.proxy.getAllPlayers();
		
		allPlayers.stream()
			.forEach(player -> player.sendMessage(message));
		
	}
	
	public void sendToDiscord(final String sender, final String message)
	{
		Discord.sendServerMessage(sender + " -> " + message).queue();
		
	}
	
	public final NamedTextColor getExpectedTextColor(final String sender)
	{
		if (Objects.equals(sender, lastSender)) return currentTextColor;
			
		lastSender = sender;
		
		if (currentTextColor == NamedTextColor.GRAY)
		{
			currentTextColor = NamedTextColor.WHITE;
		} else if (currentTextColor == NamedTextColor.WHITE)
		{
			currentTextColor = NamedTextColor.GRAY;
		}
		
		return currentTextColor;
	}
	
}
