package net.pgfmc.proxycore.bot.util;

import java.util.Collection;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;

public abstract class MessageHandler {
	
	//private static String lastSender;
	//private static NamedTextColor currentTextColor = NamedTextColor.WHITE;
	
	public static final void sendToMinecraft(final Component message)
	{
		final Collection<Player> allPlayers = Main.plugin.proxy.getAllPlayers();
		
		allPlayers.stream()
			.forEach(player -> player.sendMessage(message));
		
	}
	
	public static final void sendToDiscord(final String message)
	{
		Discord.sendServerMessage(message).queue();
		
	}
	
	public static final NamedTextColor getExpectedTextColor(final String sender)
	{
		return NamedTextColor.WHITE;
		
		/*
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
		*/
	}
	
}
