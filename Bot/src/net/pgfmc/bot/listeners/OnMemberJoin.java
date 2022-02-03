package net.pgfmc.bot.listeners;

import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.functions.spam.Raid;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.CoreMain.Machine;

public class OnMemberJoin implements EventListener {

	@Override
	public void onEvent(GenericEvent event) {
		if (!(event instanceof GuildMemberJoinEvent)) return;
		
		GuildMemberJoinEvent e = (GuildMemberJoinEvent) event;
		
		if (CoreMain.machine != Machine.MAIN) return;
		
		User user = e.getUser();
		
		EmbedBuilder eb = Discord.simpleServerEmbed(user.getAsTag() + " has joined PGF!", user.getEffectiveAvatarUrl(), Discord.gold);
		eb.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert(eb.build());
		
		Raid.check(e);
	}
	
	

}
