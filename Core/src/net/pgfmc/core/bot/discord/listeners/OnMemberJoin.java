package net.pgfmc.core.bot.discord.listeners;

import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.bot.discord.AutoRole;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.util.Colors;

public class OnMemberJoin extends ListenerAdapter {

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		
		User user = e.getUser();
		
		EmbedBuilder eb = Discord.simpleServerEmbed(user.getAsTag() + " has joined PGF Discord!", user.getEffectiveAvatarUrl(), Colors.GOLD);
		eb.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert(eb.build()).queue();
		
		AutoRole.giveMemberRole(e.getMember());
	}

}
