package net.pgfmc.core.bot.discord;

import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class AutoRole {
	
	private static final String MEMBER_ROLE_ID = "579062298526875648";
	
	public AutoRole()
	{
		Guild guild = Discord.getGuildPGF();
		Role memberRole = guild.getRoleById(MEMBER_ROLE_ID);
		
		// Get every member who doesn't have the member role (and who isn't a bot) and give it to them
		guild.getMembers().stream()
		.filter(member -> !member.getRoles().contains(memberRole) && !member.getUser().isBot())
		.collect(Collectors.toList()).stream()
		.forEach(member -> guild.addRoleToMember(member, memberRole).queue());
	}
	
	public static void giveMemberRole(Member member)
	{
		if (member.getUser().isBot()) return;
		
		Guild guild = Discord.getGuildPGF();
		Role memberRole = guild.getRoleById(MEMBER_ROLE_ID);
		
		guild.addRoleToMember(member, memberRole).queue();
	}

}
