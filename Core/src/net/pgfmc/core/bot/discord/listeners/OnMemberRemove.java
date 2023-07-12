package net.pgfmc.core.bot.discord.listeners;

import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.util.Colors;

public class OnMemberRemove extends ListenerAdapter {
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		
		User user = e.getUser();
		
		EmbedBuilder eb = Discord.simpleServerEmbed(user.getEffectiveName() + " (@" + user.getName() + ") left.", user.getEffectiveAvatarUrl(), Colors.RED)
								.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert(eb.build()).queue();
		
	}

}
