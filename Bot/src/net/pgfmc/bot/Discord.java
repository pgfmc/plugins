package net.pgfmc.bot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.bukkit.OfflinePlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.pgfmc.bot.listeners.OnMessageReceived;
import net.pgfmc.bot.listeners.OnReady;
import net.pgfmc.bot.listeners.OnSlashCommand;
import net.pgfmc.bot.listeners.OnUpdateRole;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA = null;
	public static String PREFIX = "!";
	public static List<String> ADMINS = new ArrayList<>(Arrays.asList("243499063838769152", "440726027405361152")); // bk, Crimson
	private static TextChannel serverChannel;
	
	public static String PGF_ID = "579055447437475851";
	public static String BTS_ID = "721951670132801596";
	
	// Frequently used colors
	public static final Color green = new Color(0, 255, 0); // Join
	public static final Color red = new Color(255, 0, 0); // Leave
	public static final Color gold = new Color(255, 215, 0); // Advancement
	public static final Color black = new Color(0, 0, 0); // Death
	
	public static String SERVER_CHANNEL;
	public static final String START_MESSAGE = "<:START:905682398790959125> Server has started!";
	public static final MessageEmbed START_MESSAGE_EMBED = simpleServerEmbed("Server is starting...", "https://cdn.discordapp.com/emojis/905682398790959125.png?size=44", green);
	public static final String STOP_MESSAGE = "<:STOP:905683316844429312> Server is stopping...";
	public static final MessageEmbed STOP_MESSAGE_EMBED = simpleServerEmbed("Server is stopping...", "https://cdn.discordapp.com/emojis/905683316844429312.png?size=44", red);
	
	public static void initialize() throws LoginException, InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(Secret.getKey()); // bot token, don't share.
		/*
		 * Register EventListeners here ---
		 */
		
		builder.addEventListeners(new OnReady());
		builder.addEventListeners(new OnMessageReceived());
		builder.addEventListeners(new OnUpdateRole());
		builder.addEventListeners(new OnSlashCommand());
		
		/*
		 * 
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
		if (m == null || m == "") {
			return;
		}
		serverChannel.sendMessage(m).queue();
		System.out.println("Discord: " + m);
	}
	
	public static void sendAlert(String m) {
		if (m == null || m.equals("")) { return; }
		JDA.getTextChannelById("891939656969621534").sendMessage(m).queue();
	}
	public static void sendAlert(MessageEmbed me) {
		if (me == null) { return; }
		JDA.getTextChannelById("891939656969621534").sendMessage(me).queue();
	}
	
	public static void setChannel(TextChannel server) {
		if (serverChannel == null) {
			serverChannel = server;
		}
	}
	
	public static TextChannel getChannel() {
		return serverChannel;
	}
	
	
	
	public static void sendEmbed(MessageEmbed eb)
	{
		serverChannel.sendMessage(eb).queue();
		// System.out.println("Discord: " + eb.getAuthor().getName());
	}
	
	
	
	/**
	 * For more complicated embeds, use EmbedBuilder from JDA
	 * @return
	 */
	
	public static MessageEmbed simplePlayerEmbed(OfflinePlayer player, String message, Color color)
	{
		EmbedBuilder eb = new EmbedBuilder();
		
		// Thank you to "https://crafatar.com" for providing avatars.
		eb.setColor(color);
		eb.setAuthor(player.getName() + " " + message, null, "https://crafatar.com/avatars/" + player.getUniqueId());
		
		return eb.build();
	}
	
	public static MessageEmbed simpleServerEmbed(String message, String icon, Color color)
	{
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setColor(color);
		eb.setAuthor(message, null, icon);
		
		return eb.build();
	}
	
	public static List<String> getMemberRoles(String id)
	{
		if (id == null) return null;
		
		return JDA.getGuildById(PGF_ID)
		.getMemberById(id)
		.getRoles().stream()
		.map(role -> role.getId())
		.collect(Collectors.toList());
	}
}
