package net.pgfmc.proxycore.bot.discord.listeners;

import java.util.Objects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.roles.PGFRole;

public class OnMessageReceived extends MessageHandler {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e)
	{
		// return if message isn't in #server or if the user is a bot
		if (!Objects.equals(e.getChannel().asTextChannel(), Discord.getServerChannel())) return;
		
		final JDA jda = e.getJDA();
		final User user = e.getAuthor();
		final Message message = e.getMessage();
		
		if (user.isBot()) return;
		if (message.getContentDisplay().length() == 0) return;
		
		/*
		if (Profanity.hasProfanity(handler.getMessage()) && e.getGuild().equals(Discord.getGuildPGF()))
		{
			e.getChannel().sendMessage(user.getAsMention() + ", please do not use blacklisted words!");
			e.getMessage().delete().queue();
			
			EmbedBuilder eb = Discord.simpleServerEmbed(user.getEffectiveName() + " (@" + user.getName() + ")", user.getAvatarUrl(), Colors.RED)
									.setTitle("Blacklisted word detected! (Discord)")
									.setDescription("A blacklisted word was detected by " + user.getName() + " in Discord.")
									.addField("User", user.getName(), false)
									.addField("Message", "|| " + handler.getMessage() + " ||", false)
									.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build()).queue();
			return;
		}
		
		*/
		
		final Guild pgfGuild = jda.getGuildById(Discord.GUILD_ID_PGF);
		PGFRole memberRole = PGFRole.MEMBER;
		
		// If member of PGF (mainly for BTS/outside PGF server)
		if (pgfGuild.isMember(user))
		{
			memberRole = Discord.getTopRoleOfMember(pgfGuild.getMember(user));
			
		}
		
		final Builder textComponentBuilder = Component.text();
		
		// If it isn't a reply
		if(message.getReferencedMessage() == null || message.getReferencedMessage().getAuthor().isBot())
		{
			textComponentBuilder.append(
					Component.text(((memberRole.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + e.getMember().getEffectiveName())
						.color(memberRole.getColor()));
			
			textComponentBuilder.append(
					Component.text(" -|| ")
						.color(NamedTextColor.GRAY));
			
			textComponentBuilder.append(
					Component.text(message.getContentDisplay()))
						.color(getExpectedTextColor(user.getEffectiveName()));
			
			sendToMinecraft(textComponentBuilder.build());
			
			return;
		} else { // is reply
			final Member replyMember = pgfGuild.getMember(e.getMessage().getReferencedMessage().getAuthor());
            PGFRole replyRole = PGFRole.MEMBER;
            
            if (replyMember != null)
            {
            	replyRole = Discord.getTopRoleOfMember(replyMember);
            	
            }
            
            textComponentBuilder.append(
					Component.text(((memberRole.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + e.getMember().getEffectiveName() + " replied to ")
						.color(memberRole.getColor()));
            
            textComponentBuilder.append(
					Component.text(((memberRole.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + replyMember.getEffectiveName())
						.color(replyRole.getColor()));
			
			textComponentBuilder.append(
					Component.text(" -|| ")
						.color(NamedTextColor.GRAY));
			
			textComponentBuilder.append(
					Component.text(message.getContentDisplay()))
						.color(getExpectedTextColor(user.getEffectiveName()));
			
			sendToMinecraft(textComponentBuilder.build());
            
            return;
		}
		
	}
	
}

