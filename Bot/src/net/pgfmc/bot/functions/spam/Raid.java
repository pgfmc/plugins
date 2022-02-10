package net.pgfmc.bot.functions.spam;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.pgfmc.bot.Discord;

public class Raid {
	
	private static LinkedList<String> last5MemberIds = new LinkedList<>();
	
	private static long lastJoin = 0L;
	private static double joinPerSecond = 0;

	public static void check(GuildMemberJoinEvent e) {
		Guild g = e.getGuild();
		
		if (!g.getId().equals(Discord.getGuildPGF().getId())) return;
		
		double lastJoinInSeconds = (double) ((new Date().getTime() - lastJoin) / 1000);
		double joins = joinPerSecond * lastJoinInSeconds + 1;
		
		joinPerSecond = joins / lastJoin;
		lastJoin = new Date().getTime();
		
		if (last5MemberIds.size() > 5)
		{
			last5MemberIds.remove(0);
		}
		
		last5MemberIds.add(e.getMember().getId());
		
		if (joinPerSecond >= 5)
		{
			Role muteRole = g.getRoleById("895443829031862342");
			for (String id : last5MemberIds)
			{
				g.addRoleToMember(id, muteRole);
			}
			
			EmbedBuilder eb = Discord.simpleServerEmbed("Raid detected!", "https://cdn.discordapp.com/emojis/726548471015800912.webp?size=96", Discord.RED);
			eb.setDescription("Last 5 members: " + last5MemberIds.stream()
													.map(id -> g.getMemberById(id).getAsMention())
													.collect(Collectors.toList())
					);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build()).queue();
			
		}
		
	}

}
