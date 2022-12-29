package net.pgfmc.bot.discord.listeners;

import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.discord.AutoRole;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.util.Colors;

public class OnMemberJoin implements EventListener {

	@Override
	public void onEvent(GenericEvent event) {
		if (!(event instanceof GuildMemberJoinEvent)) return;
		
		GuildMemberJoinEvent e = (GuildMemberJoinEvent) event;
		
		// If the event's guild doesn't match the #server guild
		if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		
		User user = e.getUser();
		
		EmbedBuilder eb = Discord.simpleServerEmbed(user.getAsTag() + " has joined PGF Discord!", user.getEffectiveAvatarUrl(), Colors.GOLD);
		eb.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert(eb.build()).queue();
		
		new AutoRole().autoRole(e.getMember());
	}

}
