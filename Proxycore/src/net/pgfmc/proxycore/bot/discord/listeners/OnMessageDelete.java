package net.pgfmc.proxycore.bot.discord.listeners;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;

public class OnMessageDelete extends ListenerAdapter {
	
	@Override
	public void onMessageDelete(MessageDeleteEvent e)
	{
		// If the event's guild doesn't match the #server guild
		if (!Objects.equals(e.getGuild(), Discord.getAssociatedGuild())) return;
		if (!e.isFromGuild()) return;
		
		final String messageId = e.getMessageId();
		final Message message = Discord.getMessageFromHistory(messageId);
		
		if (message == null) return;
		if (message.getAuthor().isBot()) return;
		
		final User user = message.getAuthor();
		
		final EmbedBuilder builder = Discord.createServerEmbed("A message was deleted.", user.getEffectiveAvatarUrl(), NamedTextColor.BLACK)
				.setTimestamp(OffsetDateTime.now())
				.setDescription(message.getContentRaw())
				.setFooter(user.getEffectiveName() + " (@" + user.getName() + ")");
		
		Discord.sendAlertMessageEmbed(builder.build()).queue();
		
		final List<Attachment> attachments = message.getAttachments();
		
		if (attachments.size() == 0) return;
		
		for (final Attachment attachment : attachments)
		{
			Discord.sendAlertMessage(attachment.getUrl()).queue();
		}
		
	}

}

