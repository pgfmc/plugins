package net.pgfmc.core.bot.discord.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.roles.Roles;

public class OnUpdateRole extends ListenerAdapter {

	@Override
	public void onGenericGuildMember(GenericGuildMemberEvent e)
	{
		Member member = e.getMember();
		
		PlayerData pd = PlayerData.fromDiscordId(member.getId());
		
		if (pd == null) return;
		
		Roles.setRole(pd);
	}
}
