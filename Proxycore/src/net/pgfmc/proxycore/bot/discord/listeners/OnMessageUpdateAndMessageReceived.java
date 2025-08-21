package net.pgfmc.proxycore.bot.discord.listeners;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;

public class OnMessageUpdateAndMessageReceived extends ListenerAdapter {
	
	private static final Map<String, Message> MESSAGE_HISTORY = new HashMap<>();
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if (!e.isFromGuild()) return;
		
		MESSAGE_HISTORY.put(e.getMessageId(), e.getMessage());
		
	}
	
	@Override
	public void onMessageUpdate(MessageUpdateEvent e)
	{
		if (!e.isFromGuild()) return;
		// If the event's guild doesn't match the #server guild
		if (!Objects.equals(e.getGuild(), Discord.getAssociatedGuild())) return;
		
		final Message oldMessage = Discord.getMessageFromHistory(e.getMessageId());
		final Message updatedMessage = e.getMessage();
		
		if (oldMessage == null) return;
		if (oldMessage.getAuthor().isBot()) return;
		if (Objects.equals(oldMessage.getContentDisplay(), updatedMessage.getContentDisplay())) return;
		
		final User user = oldMessage.getAuthor();
		final EmbedBuilder builder = new EmbedBuilder()
			.setColor(new Color(NamedTextColor.BLACK.value()))
			.setAuthor("A message was updated.", e.getJumpUrl(), user.getEffectiveAvatarUrl())
			.setDescription(oldMessage.getContentRaw())
			.setFooter(user.getEffectiveName() + " (@" + user.getName() + ")")
			.setTimestamp(OffsetDateTime.now());
		
		Discord.sendAlertMessageEmbed(builder.build()).queue();
		
		final List<Attachment> attachments = oldMessage.getAttachments();
		
		if (attachments.size() == 0) return;
		
		for (final Attachment attachment : attachments)
		{
			Discord.sendAlertMessage(attachment.getUrl()).queue();
		}
		
		// Update the message in message_history
		MESSAGE_HISTORY.put(e.getMessageId(), updatedMessage);
		
	}
	
	public static final Map<String, Message> getMessageHistory()
	{
		return MESSAGE_HISTORY;
	}

}
