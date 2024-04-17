package net.pgfmc.core.bot.util;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;

public class MessageHandler extends ListenerAdapter {
	
	private enum SenderType {
		DISCORD,
		MINECRAFT;
	}
	
	private static String lastSender = "";
	private static boolean altColor = false;
	
	private SenderType type;
	private String message = "";
	private User user = null;
	private OfflinePlayer player = null;
	
	private static Map<String, Message> message_history = new HashMap<>();
	
	public MessageHandler(User sender)
	{
		type = SenderType.DISCORD;
		this.user = sender;
	}
	
	public MessageHandler(OfflinePlayer sender)
	{
		type = SenderType.MINECRAFT;
		this.player = sender;
	}
	
	public MessageHandler setMessage(String message)
	{
		this.message = message;
		return this;
	}
	
	public SenderType getSenderType()
	{
		return type;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public OfflinePlayer getPlayer()
	{
		return player;
	}
	
	public void send()
	{
		if (type == SenderType.DISCORD)
		{
			Bukkit.getServer().broadcastMessage(message);
		} else if (type == SenderType.MINECRAFT)
		{			
			Discord.sendMessage(ChatColor.stripColor(PlayerData.from(player).getRankedName()) + " -> " + message).queue();
		}
		
	}
	
	public static ChatColor getTrackColor(String sender)
	{
		if (sender.equals(lastSender)) return (altColor) ? ChatColor.GRAY : ChatColor.WHITE;
			
		lastSender = sender;
		altColor = !altColor;
		return (altColor) ? ChatColor.GRAY : ChatColor.WHITE;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if (!e.isFromGuild()) return;
		
		message_history.put(e.getMessageId(), e.getMessage());
		
		
	}
	
	@Override
	public void onMessageUpdate(MessageUpdateEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		if (!e.isFromGuild()) return;
		
		Message message = MessageHandler.getMessage(e.getMessageId());
		
		if (message == null) return;
		if (message.getAuthor().isBot()) return;
		
		User user = message.getAuthor();
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setColor(Colors.BLACK.getColor());
		eb.setAuthor("A message was updated.", e.getJumpUrl(), user.getEffectiveAvatarUrl());
		eb.setDescription(message.getContentRaw());
		eb.setFooter(user.getEffectiveName() + " (@" + user.getName() + ")");
		eb.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlert(eb.build()).queue();
		
		List<Attachment> attachments = message.getAttachments();
		
		if (attachments.size() == 0) return;
		
		for (Attachment attachment : attachments)
		{
			Discord.sendAlert(attachment.getUrl()).queue();
		}
		
		// Update the message in message_history
		message_history.put(e.getMessageId(), e.getMessage());
		
	}
	
	public static Message getMessage(String message_id)
	{
		return message_history.get(message_id);
	}
	
}






