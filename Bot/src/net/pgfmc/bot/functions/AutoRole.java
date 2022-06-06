package net.pgfmc.bot.functions;

import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.pgfmc.bot.Discord;

public class AutoRole {
	
	private static final String MEMBER_ROLE_ID = "579062298526875648";
	
	public void maintainRole()
	{
		Guild guild = Discord.getGuildPGF();
		Role memberRole = guild.getRoleById(MEMBER_ROLE_ID);
		
		List<Member> noMemberRole = guild.getMembers().stream().filter(member -> !member.getRoles().contains(memberRole)).collect(Collectors.toList());
		
		for (Member m : noMemberRole)
		{
			if (m.getUser().isBot()) continue;
			
			guild.addRoleToMember(m, memberRole).queue();
		}
	}
	
	public void autoRole(Member member)
	{
		if (member.getUser().isBot()) return;
		
		Guild guild = Discord.getGuildPGF();
		Role memberRole = guild.getRoleById(MEMBER_ROLE_ID);
		
		guild.addRoleToMember(member, memberRole).queue();
	}

}
