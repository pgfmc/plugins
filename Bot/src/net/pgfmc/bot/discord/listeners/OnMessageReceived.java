package net.pgfmc.bot.discord.listeners;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.util.Colors;
import net.pgfmc.bot.util.MessageHandler;
import net.pgfmc.core.chat.Profanity;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.permissions.Roles.PGFRole;

public class OnMessageReceived extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent e)
	{
		final User user = e.getAuthor();
		
		final MessageHandler handler = new MessageHandler(e.getMessage().getContentDisplay(), user);
		
		if (handler.getMessage().length() == 0) return;
		
		if (Profanity.hasProfanity(handler.getMessage()) && e.getGuild().equals(Discord.getGuildPGF()))
		{
			e.getChannel().sendMessage(user.getAsMention() + ", please do not use blacklisted words!");
			e.getMessage().delete().queue();
			
			EmbedBuilder eb = Discord.simpleServerEmbed(user.getAsTag(), user.getAvatarUrl(), Colors.RED);
			eb.setTitle("Blacklisted word detected! (Discord)");
			eb.setDescription("A blacklisted word was detected by " + user.getName() + " in Discord.");
			eb.addField("User", user.getName(), false);
			eb.addField("Message", "|| " + handler.getMessage() + " ||", false);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build()).queue();
			return;
		}
		
		// return if message isn't in #server or a bot
		if (!e.getChannel().getId().equals(Discord.getServerChannel().getId()) || user.isBot()) return;
		
		PGFRole memberRole = PGFRole.MEMBER;
		
		// If member of PGF (mainly for BTS/outside PGF server)
		if (Discord.getGuildPGF().isMember(user))
		{
			memberRole = Roles.getTop(Discord.getMemberRoles(user.getId())
					.stream()
					.map(roleName -> PGFRole.get(roleName))
					.collect(Collectors.toList()).stream()
					.filter(role -> role != null)
					.collect(Collectors.toList()));
			
		}
		
		// If not reply
		if(e.getMessage().getReferencedMessage() == null || e.getMessage().getReferencedMessage().getAuthor().isBot() || Discord.getGuildPGF() == null)
		{
			handler.setMessage(memberRole.getColor()
					+ e.getMember().getEffectiveName()
					+ " " + ChatColor.RESET + ChatColor.DARK_GRAY + "-|| "
					+ MessageHandler.getTrackColor(e.getMember().getId())
					+ handler.getMessage());
			
			handler.send();
			
			return;
		} else {
            PGFRole replyRole = PGFRole.MEMBER;
            Member replyMember = Discord.getGuildPGF().getMember(e.getMessage().getReferencedMessage().getAuthor());
            
            if (replyMember != null)
            {
            	replyRole = Roles.getTop(Discord.getMemberRoles(replyMember.getId())
						.stream()
						.map(roleName -> PGFRole.get(roleName))
						.collect(Collectors.toList()).stream()
						.filter(role -> role != null)
						.collect(Collectors.toList()));
            }
            
            handler.setMessage(memberRole.getColor()
            		+ e.getMember().getEffectiveName()
            		+ " replied to "
            		+ replyRole.getColor()
            		+ replyMember.getEffectiveName()
            		+ " " + ChatColor.RESET + ChatColor.DARK_GRAY + "|| "
            		+ MessageHandler.getTrackColor(e.getMember().getId())
            		+ handler.getMessage());
            
            handler.send();
            
            return;
            
		}
		
	}
	
}

