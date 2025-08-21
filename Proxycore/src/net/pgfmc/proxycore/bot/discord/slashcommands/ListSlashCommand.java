package net.pgfmc.proxycore.bot.discord.slashcommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.bot.Discord;

public class ListSlashCommand {
	
	public ListSlashCommand(final SlashCommandInteractionEvent e)
	{
		if (!Objects.equals(e.getGuild(), Discord.getAssociatedGuild())) return;
		
		final Collection<Player> allOnlinePlayers = Main.plugin.proxy.getAllPlayers();
		final List<String> names = new ArrayList<>();
		
		for (final Player player : allOnlinePlayers)
		{
			names.add(player.getUsername());
		}
		
		e.reply("Online Players: " + names.toString()).queue();
		
	}

}
