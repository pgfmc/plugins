package net.pgfmc.proxycore.bot;

import java.util.List;

import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.discord.listeners.OnMemberJoin;
import net.pgfmc.proxycore.bot.discord.listeners.OnMemberRemove;
import net.pgfmc.proxycore.bot.discord.listeners.OnMessageDelete;
import net.pgfmc.proxycore.bot.discord.listeners.OnMessageReceived;
import net.pgfmc.proxycore.bot.discord.listeners.OnMessageUpdateAndMessageReceived;
import net.pgfmc.proxycore.bot.discord.listeners.OnReady;
import net.pgfmc.proxycore.bot.discord.listeners.OnSlashCommand;
import net.pgfmc.proxycore.bot.discord.listeners.OnUpdateRole;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.roles.PGFRole;

public final class Discord extends ListenerAdapter {
	public static final long GUILD_ID_PGF = 579055447437475851L;
	
	private static JDA jda;
	private static long alertChannelId;
	private static long serverChannelId;
	
	public Discord(final String token, final long alertChannel, final long serverChannel) throws InterruptedException
	{
		alertChannelId = alertChannel;
		serverChannelId = serverChannel;
		
		jda = JDABuilder.createDefault(token)
			.addEventListeners(new OnReady(this),
							   new OnMessageReceived(),
							   new OnUpdateRole(),
							   new OnSlashCommand(),
							   new OnMemberJoin(),
							   new OnMemberRemove(),
							   new OnMessageDelete(),
							   new OnMessageUpdateAndMessageReceived())
			.setChunkingFilter(ChunkingFilter.ALL)
			.setMemberCachePolicy(MemberCachePolicy.ALL)
			.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
		.build()
		.awaitReady();
		
	}
	
	public final void setJda(final JDA jda)
	{
		Discord.jda = jda;
	}
	
	public static final MessageCreateAction sendServerMessage(final String message)
	{
		if (message == null || message.isBlank()) return null;
		return getServerChannel().sendMessage(message);
	}
	
	public static final MessageCreateAction sendServerMessageEmbed(final MessageEmbed embed)
	{
		if (embed == null) return null;
		return getServerChannel().sendMessageEmbeds(embed);
	}
	
	public static final MessageCreateAction sendAlertMessage(final String message) {
		if (message == null || message.isBlank()) return null;
		return getAlertChannel().sendMessage(message);
	}
	
	public static final MessageCreateAction sendAlertMessageEmbed(final MessageEmbed embed) {
		if (embed == null) return null;
		return getAlertChannel().sendMessageEmbeds(embed);
	}
	
	public static final TextChannel getServerChannel() {
		return jda.getTextChannelById(serverChannelId);
	}
	
	public static final TextChannel getAlertChannel() {
		return jda.getTextChannelById(alertChannelId);
	}
	
	/**
	 * Gets the guild that instance is associated with (likely either BTS or PGF).
	 * Defined by the guild belonging to the server channel.
	 * 
	 * @return The guild this instance is associated with
	 */
	public static final Guild getAssociatedGuild()
	{
		return getServerChannel().getGuild();
	}
	
	public static final JDA getJda()
	{
		return jda;
	}
	
	/**
	 * 	Makes a simple embed for a Player
	 * 
	 * @param player The player
	 * @param title "Player " + title
	 * @param color The color of the embed
	 * @return the EmbedBuilder
	 */
	public static final EmbedBuilder createPlayerEmbed(final Player player, final String playerDidWhat, final NamedTextColor color)
	{
		final EmbedBuilder builder = new EmbedBuilder();
		
		// Thank you to "https://crafatar.com" for providing avatars.
		builder.setColor(color.value());
		builder.setAuthor(player + " " + playerDidWhat, null, "https://crafatar.com/avatars/" + player.getUniqueId());
		
		return builder;
	}
	
	/**
	 * Makes a simple embed for the Discord server
	 * 
	 * @param title The title
	 * @param icon The icon of the embed
	 * @param color The color
	 * @return the EmbedBuilder
	 */
	public static final EmbedBuilder createServerEmbed(final String author, final String iconLink, final NamedTextColor color)
	{
		final EmbedBuilder builder = new EmbedBuilder();
		
		builder.setColor(color.value());
		builder.setAuthor(author, null, iconLink);
		
		return builder;
	}
	
	/**
	 * Get a list of roles this user has
	 * 
	 * @param id The ID of the user
	 * @return A list of roles this user has
	 */
	public static final PGFRole getTopRoleOfMember(final Member member)
	{
		if (member == null) return PGFRole.MEMBER;
		
		final Guild pgfGuild = jda.getGuildById(GUILD_ID_PGF);
		
		if (!pgfGuild.isMember(member)) return PGFRole.MEMBER;
		
		final List<Role> discordRoles = member.getRoles();
		
		if (discordRoles.isEmpty()) return PGFRole.MEMBER;
		
		final PGFRole role = PGFRole.get(discordRoles.get(0).getName());
		
		if (role == null) return PGFRole.MEMBER;
		
		return role;
	}
	
	public static final Message getMessageFromHistory(String message_id)
	{
		return OnMessageUpdateAndMessageReceived.getMessageHistory().get(message_id);
	}
	
	/**
	 * Takes in a message and attempts to convert the Discord mentions from "@user" to "<@theUserId>"
	 * 
	 * @param message The message to convert
	 * @return The converted message
	 */
	public static String convertDiscordMentions(String message)
	{
		if (!message.contains("@")) return message;
		
		final String workingMessage = new String(message);
		
		int startOfMention = workingMessage.indexOf("@");                      // 0 1 2 3 4 5 6
		int endOfMention = workingMessage.indexOf(" ", startOfMention);        // - - @ b k - -
		
		if (startOfMention == workingMessage.length() - 1) return message;
		
		if (endOfMention == -1)
		{
			endOfMention = workingMessage.length();
		}
		
		final String username = workingMessage.substring(startOfMention + 1, endOfMention);
		Logger.debug("Name to match: " + username);
		
		final List<Member> nicknameMatches = getJda().getGuildById(GUILD_ID_PGF).getMembersByEffectiveName(username, true);
		final List<Member> usernameMatches = getJda().getGuildById(GUILD_ID_PGF).getMembersByName(username, true);
		
		if (nicknameMatches == null || nicknameMatches.isEmpty())
		{
			if (usernameMatches == null || usernameMatches.isEmpty()) return message;
			
			return workingMessage.replaceAll(workingMessage.substring(startOfMention, endOfMention), usernameMatches.get(0).getAsMention());
		}
		
		return workingMessage.replaceAll(workingMessage.substring(startOfMention, endOfMention), nicknameMatches.get(0).getAsMention());
	}
	
}

