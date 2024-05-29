package net.pgfmc.proxycore.bot.discord.listeners;

import java.util.Objects;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.discord.slashcommands.LinkSlashCommand;
import net.pgfmc.proxycore.bot.discord.slashcommands.ListSlashCommand;

public class OnSlashCommand extends ListenerAdapter {
    
	@Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
	{
		if (!Objects.equals(e.getGuild(), Discord.getAssociatedGuild())) return;
		
        if (e.getName().equals("list")) new ListSlashCommand(e);
        if (e.getName().equals("link")) new LinkSlashCommand(e);
      
    }
	
}
