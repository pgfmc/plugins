package net.pgfmc.core.bot.discord;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.bot.discord.listeners.OnMemberJoin;
import net.pgfmc.core.bot.discord.listeners.OnMemberRemove;
import net.pgfmc.core.bot.discord.listeners.OnMessageDelete;
import net.pgfmc.core.bot.discord.listeners.OnMessageReceived;
import net.pgfmc.core.bot.discord.listeners.OnReady;
import net.pgfmc.core.bot.discord.listeners.OnSlashCommand;
import net.pgfmc.core.bot.discord.listeners.OnUpdateRole;
import net.pgfmc.core.bot.util.Colors;
import net.pgfmc.core.bot.util.MessageHandler;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA;
	public static final String PREFIX = "!";
	public static final String CHANNEL_ALERT = CoreMain.plugin.getConfig().getString("alert-channel");
	public static final String CHANNEL_SERVER = CoreMain.plugin.getConfig().getString("server-channel");
	
	public Discord() throws InterruptedException
	{
		JDABuilder.createDefault(CoreMain.plugin.getConfig().getString("token"))
						.addEventListeners(new OnReady(),
										   new OnMessageReceived(),
										   new OnUpdateRole(),
										   new OnSlashCommand(),
										   new OnMemberJoin(),
										   new OnMemberRemove(),
										   new OnMessageDelete(),
										   new MessageHandler((User) null))
						.setChunkingFilter(ChunkingFilter.ALL)
						.setMemberCachePolicy(MemberCachePolicy.ALL)
						.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
					.build()
					.awaitReady();
		
	}
	
	public static MessageCreateAction sendMessage(String message)
	{
		if (message == null || message == "") return null;
		return getServerChannel().sendMessage(message);
	}
	
	public static MessageCreateAction sendMessage(MessageEmbed embed)
	{
		if (embed == null) return null;
		return getServerChannel().sendMessageEmbeds(embed);
	}
	
	public static MessageCreateAction sendAlert(String message) {
		if (message == null || message.equals("")) { return null; }
		return getAlertChannel().sendMessage(message);
	}
	
	public static MessageCreateAction sendAlert(MessageEmbed embed) {
		if (embed == null) { return null; }
		return getAlertChannel().sendMessageEmbeds(embed);
	}
	
	public static TextChannel getServerChannel() {
		return JDA.getTextChannelById(CHANNEL_SERVER);
	}
	
	public static TextChannel getAlertChannel() {
		return JDA.getTextChannelById(CHANNEL_ALERT);
	}
	
	public static Guild getGuildPGF()
	{
		return JDA.getGuildById("579055447437475851");
	}
	
	
	/**
	 * 	Makes a simple embed for a Player
	 * 
	 * @param player The player
	 * @param title "Player " + title
	 * @param color The color of the embed
	 * @return the EmbedBuilder
	 */
	public static EmbedBuilder simplePlayerEmbed(OfflinePlayer player, String playerDidWhat, Colors color)
	{
		EmbedBuilder eb = new EmbedBuilder();
		
		// Thank you to "https://crafatar.com" for providing avatars.
		eb.setColor(color.getColor());
		eb.setAuthor(player.getName() + " " + playerDidWhat, null, "https://crafatar.com/avatars/" + player.getUniqueId());
		
		return eb;
	}
	
	/**
	 * Makes a simple embed for the Discord server
	 * 
	 * @param title The title
	 * @param icon The icon of the embed
	 * @param color The color
	 * @return the EmbedBuilder
	 */
	public static EmbedBuilder simpleServerEmbed(String author, String iconLink, Colors color)
	{
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setColor(color.getColor());
		eb.setAuthor(author, null, iconLink);
		
		return eb;
	}
	
	/**
	 * Gets a list of the Member's Discord Role names
	 * 
	 * @param id The Member's Discord ID
	 * @return The Role name list in lowercase
	 */
	public static List<String> getMemberRoles(String id)
	{
		if (id == null) return null;
		
		List<String> discordRoles = null;
		
		try {
		discordRoles = getGuildPGF()
		.getMemberById(id)
		.getRoles().stream()
		.map(role -> role.getName().toLowerCase())
		.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (discordRoles == null || discordRoles.isEmpty()) return new ArrayList<String>(Set.of("member"));
		
		return discordRoles;
		
		
	}
	
	/**
	 * Takes in a message and attempts to convert the Discord mentions from "@user" to "<@theUserId>"
	 * @param message The message to convert
	 * @return The converted message
	 */
	public static String getMessageWithDiscordMentions(String message)
	{
		if (!message.contains("@")) return message;
		
		String workingMessage = message;
		
		int startOfMention = workingMessage.indexOf("@");                      // 0 1 2 3 4 5 6
		int endOfMention = workingMessage.indexOf(" ", startOfMention);        // - - @ b k - -
		
		if (startOfMention == workingMessage.length() - 1) return message;
		
		if (endOfMention == -1)
		{
			endOfMention = workingMessage.length();
		}
		
		String username = workingMessage.substring(startOfMention + 1, endOfMention);
		Bukkit.getLogger().warning("Name to match: " + username);
		
		List<Member> nicknameMatches = Discord.getGuildPGF().getMembersByEffectiveName(username, true);
		List<Member> usernameMatches = Discord.getGuildPGF().getMembersByName(username, true);
		
		if (nicknameMatches == null || nicknameMatches.isEmpty())
		{
			
			if (usernameMatches == null || usernameMatches.isEmpty()) return message;
			
			workingMessage = workingMessage.replaceAll(workingMessage.substring(startOfMention, endOfMention), usernameMatches.get(0).getAsMention());
			
			return workingMessage;
		}
		
		workingMessage = workingMessage.replaceAll(workingMessage.substring(startOfMention, endOfMention), nicknameMatches.get(0).getAsMention());
		
		return workingMessage;
	}
	
}

