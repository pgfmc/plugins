package net.pgfmc.core.bot.discord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.bukkit.OfflinePlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.bot.discord.listeners.OnMemberJoin;
import net.pgfmc.core.bot.discord.listeners.OnMessageReceived;
import net.pgfmc.core.bot.discord.listeners.OnReady;
import net.pgfmc.core.bot.discord.listeners.OnSlashCommand;
import net.pgfmc.core.bot.discord.listeners.OnUpdateRole;
import net.pgfmc.core.bot.util.Colors;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA;
	public static final String PREFIX = "!";
	public static final String CHANNEL_ALERT = CoreMain.plugin.getConfig().getString("alert-channel");
	public static final String CHANNEL_SERVER = CoreMain.plugin.getConfig().getString("server-channel");
	
	public Discord()
	{
		// Tries to initialize discord integration
		try {
			initialize();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private final void initialize() throws LoginException, InterruptedException
	{
		JDABuilder builder = JDABuilder.createDefault(CoreMain.plugin.getConfig().getString("token")); // bot token, don't share.
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
		JDA = builder
		.setChunkingFilter(ChunkingFilter.ALL)
		.setMemberCachePolicy(MemberCachePolicy.ALL)
		.enableIntents(GatewayIntent.GUILD_MEMBERS).build();
		
		JDA.awaitReady();
		
	}
	
	public static MessageCreateAction sendMessage(String message)
	{
		if (message == null || message == "") return null;
		
		return getServerChannel().sendMessage(message);
	}
	
	public static MessageCreateAction sendEmbed(MessageEmbed embed)
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
		
		List<String> playerInvokedMention = Arrays.asList(message.substring(message.indexOf("@")).split("@"))
	    		.stream().filter(mention -> mention.contains(" ")).map(mention -> mention.substring(0, mention.indexOf(" "))).collect(Collectors.toList());
		
	    List<Member> memberNameMatches = playerInvokedMention.stream().map(mention -> Discord.getGuildPGF().getMembersByName(mention, true)).map(mentions -> mentions.get(0)).collect(Collectors.toList());
	    List<Member> memberNicknameMatches = playerInvokedMention.stream().map(mention -> Discord.getGuildPGF().getMembersByNickname(mention, true)).map(mentions -> mentions.get(0)).collect(Collectors.toList());
	    
	    Map<String, IMentionable> memberMatches = new HashMap<>();
	    
	    for (int i = 0; i < playerInvokedMention.size() - 1; i++)
	    {
	    	if (memberNicknameMatches.size() - 1 >= i)
		    {
		    	memberMatches.put(playerInvokedMention.get(i), memberNicknameMatches.get(i));
		    } else if (memberNameMatches.size() - 1 >= i)
		    {
		    	memberMatches.put(playerInvokedMention.get(i), memberNameMatches.get(i));
		    } else
		    {
		    	memberMatches.put(playerInvokedMention.get(i), null);
		    }
			
	    }
	    
	    if (!memberMatches.isEmpty())
	    {
	    	memberMatches.forEach((text, mention) -> message.replaceFirst(text, mention.getAsMention()));
	    }
	    
	    return message;
	}
	
}

