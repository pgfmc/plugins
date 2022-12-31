package net.pgfmc.bot.discord.listeners;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.discord.commands.Link;
import net.pgfmc.bot.discord.commands.List;

public class OnSlashCommand extends ListenerAdapter {
    
	@Override
    public void onSlashCommand(SlashCommandEvent e)
	{
		if (!e.isFromGuild())
		{
			if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		}
		
        if (e.getName().equals("list")) new List(e);
        if (e.getName().equals("link")) new Link(e);
      
    }
}
