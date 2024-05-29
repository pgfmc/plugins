package net.pgfmc.proxycore.bot.discord.listeners;

import java.time.OffsetDateTime;
import java.util.Objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;

public class OnMemberRemove extends ListenerAdapter {
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!Objects.equals(e.getGuild(), Discord.getAssociatedGuild())) return;
		
		final User user = e.getUser();
		
		final EmbedBuilder builder = Discord.createServerEmbed(user.getEffectiveName() + " (@" + user.getName() + ") left.", user.getEffectiveAvatarUrl(), NamedTextColor.RED)
								.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlertMessageEmbed(builder.build()).queue();
		
	}

}
