package net.pgfmc.proxycore.bot.discord.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.util.Logger;

public class OnReady extends ListenerAdapter {
	
	private final Discord discord;
	
	public OnReady(final Discord discord)
	{
		this.discord = discord;
	}

	@Override
	public void onReady(ReadyEvent e) {
		discord.setJda(e.getJDA());
		
		Logger.debug("Discord Bot initialized!");
		
		Discord.getAssociatedGuild().updateCommands().complete();
		
		Discord.getAssociatedGuild().updateCommands().addCommands(
				Commands.slash("list", "Show who's online."),
				Commands.slash("link", "Link your Minecraft account")
						.addOption(OptionType.INTEGER, "code", "The link code from Minecraft.", true))
				.queue();
		
	}

}
