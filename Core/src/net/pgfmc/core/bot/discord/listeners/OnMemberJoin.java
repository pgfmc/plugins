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
		
		EmbedBuilder eb = Discord.simpleServerEmbed(user.getEffectiveName() + " (@" + user.getName() + ") joined.", user.getEffectiveAvatarUrl(), Colors.GREEN)
								.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert(eb.build()).queue();
		
		AutoRole.giveMemberRole(e.getMember());
		
		String partying_face = new String(Character.toChars(0x1F973));
		String lrm = new String(Character.toChars(0x200E));
		
		String welcome_message = "# Welcome to PGF!\r\n"
				+ lrm + "\r\n"
				+ "The Minecraft server address is\r\n"
				+ "## |             pgfmc.net             |\r\n"
				+ lrm + "\r\n"
				+ "* You can join on **Bedrock **or **Java **using the latest version\r\n"
				+ "* Don't forget to read https://discord.com/channels/579055447437475851/679235215511519244\r\n"
				+ "\r\n"
				+ "Thanks for joining " + partying_face + " <:DONATOR:899932792106913814>";
		
		user.openPrivateChannel().queue(channel -> channel.sendMessage(welcome_message).queue());
		
	}

}
