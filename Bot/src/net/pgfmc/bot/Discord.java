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
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.pgfmc.bot.listeners.OnMemberJoin;
import net.pgfmc.bot.listeners.OnMessageReceived;
import net.pgfmc.bot.listeners.OnReady;
import net.pgfmc.bot.listeners.OnSlashCommand;
import net.pgfmc.bot.listeners.OnUpdateRole;
import net.pgfmc.core.CoreMain;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA = null;
	public static final String PREFIX = "!";
	//public static List<String> ADMINS = new ArrayList<>(Arrays.asList("243499063838769152", "440726027405361152")); // bk, Crimson
	//private static TextChannel serverChannel;
	
	//public static String PGF_ID = "579055447437475851";
	//public static String BTS_ID = "721951670132801596";
	
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
	
	public static void sendMessage(String m) {
		if (m == null || m == "") return;
		
		getServerChannel().sendMessage(m).queue();
		System.out.println("Discord: " + m);
	}
	
	public static void sendEmbed(MessageEmbed eb)
	{
		getServerChannel().sendMessage(eb).queue();
		// System.out.println("Discord: " + eb.getAuthor().getName());
	}
	
	public static void sendAlert(String m) {
		if (m == null || m.equals("")) { return; }
		getAlertChannel().sendMessage(m).queue();
	}
	
	public static void sendAlert(MessageEmbed me) {
		if (me == null) { return; }
		getAlertChannel().sendMessage(me).queue();
	}
	
	public static TextChannel getServerChannel() {
		return JDA.getTextChannelById(CoreMain.machine.getServerChannelId());
	}
	
	public static TextChannel getAlertChannel() {
		return JDA.getTextChannelById(CoreMain.machine.getAlertChannelId());
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
		
		return getGuildPGF()
		.getMemberById(id)
		.getRoles().stream()
		.map(role -> role.getId())
		.collect(Collectors.toList());
	}
}
