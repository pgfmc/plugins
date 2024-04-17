package net.pgfmc.core.bot.discord.listeners;

import java.time.OffsetDateTime;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.util.Colors;
import net.pgfmc.core.bot.util.MessageHandler;

public class OnMessageDelete extends ListenerAdapter {
	
	@Override
	public void onMessageDelete(MessageDeleteEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
		if (!e.isFromGuild()) return;
		
		final String messageId = e.getMessageId();
		Message message = MessageHandler.getMessage(messageId);
		
		if (message == null) return;
		if (message.getAuthor().isBot()) return;
		
		User user = message.getAuthor();
		
		EmbedBuilder eb = Discord.simpleServerEmbed("A message was deleted.", user.getEffectiveAvatarUrl(), Colors.BLACK).setTimestamp(OffsetDateTime.now());
		eb.setDescription(message.getContentRaw());
		eb.setFooter(user.getEffectiveName() + " (@" + user.getName() + ")");
		
		Discord.sendAlert(eb.build()).queue();
		
		List<Attachment> attachments = message.getAttachments();
		
		if (attachments.size() == 0) return;
		
		for (Attachment attachment : attachments)
		{
			Discord.sendAlert(attachment.getUrl()).queue();
		}
		
	}

}

