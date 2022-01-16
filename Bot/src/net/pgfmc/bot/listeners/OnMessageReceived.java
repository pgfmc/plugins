package net.pgfmc.bot.listeners;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.functions.AccountLinking;
import net.pgfmc.bot.functions.StartStopMessageDelete;
import net.pgfmc.bot.player.ChatEvents;
import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.permissions.Role;

public class OnMessageReceived implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof MessageReceivedEvent)) { return; }
		
		MessageReceivedEvent m = (MessageReceivedEvent) e;
		
		String s = m.getMessage().getContentDisplay();
		User user = m.getAuthor();
		Member memberPGF = Discord.JDA.getGuildById(Discord.PGF_ID).getMember(user);
		
		if (s.length() == 0) return;
		
		if (ProfanityFilter.hasProfanity(s))
		{
			m.getTextChannel().sendMessage(user.getAsMention() + ", please do not use blacklisted words!");
			m.getMessage().delete().queue();
			return;
		}
		
		// message sent in #server by a Member (not a bot)
		if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && !user.isBot()) {
			Role r = Role.MEMBER;
			// If member of PGF (mainly for BTS/outside PGF server)
			if (memberPGF != null)
			{
				r = Role.getDominantOf(memberPGF.getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
			}
			
			s.replace("%", ""); // removes all "%"s from the message.
			
			// attempts to bring over formatting from discord.
			s = format(s, "\\*\\*\\*", "§l§o"); 
			s = format(s, "\\*\\*", "§l");
			s = format(s, "\\*", "§o");
			s = format(s, "__", "§n");
			
			// If not reply
			if(m.getMessage().getReferencedMessage() == null || m.getMessage().getReferencedMessage().getAuthor().isBot())
			{
				Bukkit.getServer().broadcastMessage(r.getColorCode() + m.getMember().getEffectiveName() + " §r§8-|| " + ChatEvents.getMessageColor(m.getMember().getId()) + s);
				return;
			} else {
                User replyUser = m.getMessage().getReferencedMessage().getAuthor();
                Member replyMember = Discord.JDA.getGuildById(Discord.PGF_ID).getMember(replyUser);
                Role replyRole = Role.MEMBER;
                
                if (replyMember != null)
                {
                    replyRole = Role.getDominantOf(replyMember.getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
                }
                
                Bukkit.getServer().broadcastMessage(r.getColorCode() + m.getMember().getEffectiveName() + " replied to " + replyRole.getColorCode() + replyMember.getEffectiveName() + " §r§8-|| " + ChatEvents.getMessageColor(m.getMember().getId()) + s);
			}
			
			
			
		} else 
		
		
		// message sent to the bot in DMs.	
			
		if (m.getChannelType() == ChannelType.PRIVATE && !m.getAuthor().isBot()) {
			if (AccountLinking.linkAsk(s, user))
			{
				m.getChannel().sendMessage("Your account has been linked.").queue();
			} else
			{
				m.getChannel().sendMessage("Invalid code, please try generating a new code.").queue();
			}
			
		// if the bot sent the message.
		} else if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && user.getId().equals("721949520728031232")) {
				
			StartStopMessageDelete.run(m.getMessage());
		}
	}
	
	private String format(String s, String ds, String mc) {
		
		String[] sa = s.split(ds);
		
		boolean mark = false;
		s = "";
		
		for (String S : sa) {
			
			if (mark) {
				s = s + mc + S + "§r";
				mark = false;
			} else {
				s = s + S;
				mark = true;
			}
		}
		return s;
	}

}
