package net.pgfmc.bot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnUpdateRole implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof GuildMemberRoleAddEvent || e instanceof GuildMemberRoleRemoveEvent)) return;
		if (!((GenericGuildMemberEvent) e).getGuild().getId().equals(Discord.getGuildPGF().getId())) return;
		
		PlayerData pd = PlayerData.fromDiscordID(((GenericGuildMemberEvent) e).getMember().getId());
		if (pd == null) return;
		
		Roles.recalculate(pd);
	}
}