package net.pgfmc.core.bot.discord.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.discord.cmd.Link;
import net.pgfmc.core.bot.discord.cmd.List;

public class OnSlashCommand extends ListenerAdapter {
    
	@Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
	{
		if (!e.isFromGuild())
		{
			if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		}
		
        if (e.getName().equals("list")) new List(e);
        if (e.getName().equals("link")) new Link(e);
      
    }
}
