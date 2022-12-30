package net.pgfmc.bot.discord.commands;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class List {
	
	public List(SlashCommandInteractionEvent e)
	{
		java.util.List<String> pl = Bukkit.getOnlinePlayers()
        		.stream()
        		.map(p -> PlayerData.from(p).getDisplayName())
        		.collect(Collectors.toList());
        e.reply("Online players: " + pl).queue();
	}

}
