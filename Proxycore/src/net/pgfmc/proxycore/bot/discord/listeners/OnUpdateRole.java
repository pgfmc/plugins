package net.pgfmc.proxycore.bot.discord.listeners;


import java.util.Objects;
import java.util.UUID;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.util.roles.RoleManager;

public class OnUpdateRole extends ListenerAdapter {

	@Override
	public void onGenericGuildMember(GenericGuildMemberEvent e)
	{
		if (!(e instanceof GuildMemberUpdateEvent)) return;
		if (!Objects.equals(e.getGuild(), e.getJDA().getGuildById(Discord.GUILD_ID_PGF))) return;
		
		final Member member = e.getMember();
		final String discordUserId = member.getId();
		
		final UUID uuid = RoleManager.getPlayerUuidFromDiscordUserId(discordUserId);
		
		if (uuid == null) return; // no linked account found
		
		RoleManager.updatePlayerRole(uuid);
		
		
	}
}
