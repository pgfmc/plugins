package net.pgfmc.bot.discord.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnUpdateRole implements EventListener {

	@Override
	public void onEvent(GenericEvent event) {
		
		if (!(event instanceof GuildMemberRoleAddEvent || event instanceof GuildMemberRoleRemoveEvent)) return;
		if (!((GenericGuildMemberEvent) event).getGuild().getId().equals(Discord.getGuildPGF().getId())) return;
		
		GenericGuildMemberEvent e = (GenericGuildMemberEvent) event;
		
		Member member = e.getMember();
		
		PlayerData pd = PlayerData.fromDiscordID(member.getId());
		
		if (pd == null) return;
		
		
		Roles.setRoles(pd);
	}
}