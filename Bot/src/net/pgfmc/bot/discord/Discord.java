package net.pgfmc.bot.discord;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.bukkit.OfflinePlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.discord.listeners.OnMemberJoin;
import net.pgfmc.bot.discord.listeners.OnMessageReceived;
import net.pgfmc.bot.discord.listeners.OnReady;
import net.pgfmc.bot.discord.listeners.OnSlashCommand;
import net.pgfmc.bot.discord.listeners.OnUpdateRole;
import net.pgfmc.bot.util.Colors;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA;
	public static final String PREFIX = "!";
	
	public Discord()
	{
		// Tries to initialize discord integration
		try {
			JDA = initialize().build().awaitReady();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private final JDABuilder initialize() throws LoginException, InterruptedException
	{
		JDABuilder builder = JDABuilder.createDefault(Main.plugin.getConfig().getString("token")); // bot token, don't share.
		/*
		 * Register EventListeners here ---
		 */
		
		builder.addEventListeners(new OnReady());
		builder.addEventListeners(new OnMessageReceived());
		builder.addEventListeners(new OnUpdateRole());
		builder.addEventListeners(new OnSlashCommand());
		builder.addEventListeners(new OnMemberJoin());
		
		/*
		 * ---
		 */
		
		// Creates JDA and allows the bot to load all members 
		 return builder
		.setChunkingFilter(ChunkingFilter.ALL)
		.setMemberCachePolicy(MemberCachePolicy.ALL)
		.enableIntents(GatewayIntent.GUILD_MEMBERS);
		 
	}
	
	public static MessageAction sendMessage(String message)
	{
		if (message == null || message == "") return null;
		
		return getServerChannel().sendMessage(message);
	}
	
	public static MessageAction sendEmbed(MessageEmbed embed)
	{
		if (embed == null) return null;
		return getServerChannel().sendMessage(embed);
	}
	
	public static MessageAction sendAlert(String message) {
		if (message == null || message.equals("")) { return null; }
		return getAlertChannel().sendMessage(message);
	}
	
	public static MessageAction sendAlert(MessageEmbed embed) {
		if (embed == null) { return null; }
		return getAlertChannel().sendMessage(embed);
	}
	
	public static TextChannel getServerChannel() {
		return JDA.getTextChannelById(Main.getChannelID("server-channel"));
	}
	
	public static TextChannel getAlertChannel() {
		return JDA.getTextChannelById(Main.getChannelID("alert-channel"));
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
	
}

