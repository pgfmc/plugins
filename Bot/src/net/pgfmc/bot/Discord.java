package net.pgfmc.bot;

import java.awt.Color;
import java.util.List;
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
import net.pgfmc.bot.listeners.OnMemberJoin;
import net.pgfmc.bot.listeners.OnMessageReceived;
import net.pgfmc.bot.listeners.OnReady;
import net.pgfmc.bot.listeners.OnSlashCommand;
import net.pgfmc.bot.listeners.OnUpdateRole;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA = null;
	public static final String PREFIX = "!";
	
	// Frequently used colors
	public static final Color GREEN = new Color(0, 255, 0); // Join
	public static final Color RED = new Color(255, 0, 0); // Leave
	public static final Color GOLD = new Color(255, 215, 0); // Advancement
	public static final Color BLACK = new Color(0, 0, 0); // Death
	
	public static void initialize() throws LoginException, InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(Secret.getKey()); // bot token, don't share.
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
		
		// creates JDA and allows the bot to load all members 
		JDA = builder
		.setChunkingFilter(ChunkingFilter.ALL)
		.setMemberCachePolicy(MemberCachePolicy.ALL)
		.enableIntents(GatewayIntent.GUILD_MEMBERS)
		.build();
		
		JDA.awaitReady();
	}
	
	public static MessageAction sendMessage(String m) {
		if (m == null || m == "") return null;
		
		return getServerChannel().sendMessage(m);
	}
	
	public static MessageAction sendEmbed(MessageEmbed eb)
	{
		if (eb == null) return null;
		return getServerChannel().sendMessage(eb);
	}
	
	public static MessageAction sendAlert(String m) {
		if (m == null || m.equals("")) { return null; }
		return getAlertChannel().sendMessage(m);
	}
	
	public static MessageAction sendAlert(MessageEmbed me) {
		if (me == null) { return null; }
		return getAlertChannel().sendMessage(me);
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
	public static EmbedBuilder simplePlayerEmbed(OfflinePlayer player, String playerDidWhat, Color color)
	{
		EmbedBuilder eb = new EmbedBuilder();
		
		// Thank you to "https://crafatar.com" for providing avatars.
		eb.setColor(color);
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
	public static EmbedBuilder simpleServerEmbed(String author, String iconLink, Color color)
	{
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setColor(color);
		eb.setAuthor(author, null, iconLink);
		
		return eb;
	}
	
	public static List<String> getMemberRoles(String id)
	{
		if (id == null) return null;
		
		List<String> roles = null;
		
		try {
		roles = getGuildPGF()
		.getMemberById(id)
		.getRoles().stream()
		.map(role -> role.getId())
		.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return roles;
		
		
	}
}
