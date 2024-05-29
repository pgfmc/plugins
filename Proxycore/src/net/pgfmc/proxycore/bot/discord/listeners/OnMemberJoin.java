package net.pgfmc.proxycore.bot.discord.listeners;

import java.time.OffsetDateTime;
import java.util.Objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;

public class OnMemberJoin extends ListenerAdapter {

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!Objects.equals(e.getGuild(), Discord.getAssociatedGuild())) return;
		
		final User user = e.getUser();
		
		final EmbedBuilder builder = Discord.createServerEmbed(user.getEffectiveName() + " (@" + user.getName() + ") joined.", user.getEffectiveAvatarUrl(), NamedTextColor.GREEN)
								.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlertMessageEmbed(builder.build()).queue();
		
		// auto role
		if (Objects.equals(e.getGuild().getIdLong(), Discord.GUILD_ID_PGF))
		{
			final Guild guild = e.getGuild();
			final Role role = guild.getRoleById(579062298526875648L);
			
			guild.addRoleToMember(user, role);
		}
		
		final String partyingFace = new String(Character.toChars(0x1F973));
		final String lrm = new String(Character.toChars(0x200E)); // an empty space
		
		String welcome_message = "# Welcome to PGF!\r\n"
				+ lrm + "\r\n"
				+ "The Minecraft server address is\r\n"
				+ "## |             pgfmc.net             |\r\n"
				+ lrm + "\r\n"
				+ "* You can join on **Bedrock **or **Java **using the latest version\r\n"
				+ "* Don't forget to read https://discord.com/channels/579055447437475851/679235215511519244\r\n"
				+ "\r\n"
				+ "Thanks for joining " + partyingFace + " <:DONATOR:899932792106913814>";
		
		// Send welcome message to dms
		user.openPrivateChannel().queue(channel -> channel.sendMessage(welcome_message).queue());
		
	}

}
