package net.pgfmc.bot.listeners;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.functions.AccountLinking;
import net.pgfmc.bot.listeners.minecraft.OnAsyncPlayerChat;
import net.pgfmc.core.chat.ProfanityFilter;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.permissions.Roles.Role;

public class OnMessageReceived implements EventListener {

	@Override
	public void onEvent(GenericEvent event) {
		
		if (!(event instanceof MessageReceivedEvent)) { return; }
		
		MessageReceivedEvent e = (MessageReceivedEvent) event;
		
		String content = e.getMessage().getContentDisplay().toString().replace("//", " //");
		User user = e.getAuthor();
		Member memberPGF = Discord.getGuildPGF().getMember(user);
		// Guild g = Discord.JDA.getGuildById("579055447437475851");
		
		if (content.length() == 0) return;
		
		if (ProfanityFilter.hasProfanity(content))
		{
			e.getTextChannel().sendMessage(user.getAsMention() + ", please do not use blacklisted words!");
			e.getMessage().delete().queue();
			
			EmbedBuilder eb = Discord.simpleServerEmbed(user.getAsTag(), user.getAvatarUrl(), Discord.RED);
			eb.setTitle("Blacklisted word detected! (Discord)");
			eb.setDescription("A blacklisted word was detected by " + user.getName() + " in Discord.");
			eb.addField("User", user.getName(), false);
			eb.addField("Message", "|| " + content + " ||", false);
			eb.setTimestamp(OffsetDateTime.now());
			
			Discord.sendAlert(eb.build());
			return;
		}
		
		// message sent in #server by a Member (not a bot)
		if (e.getChannel().getId().equals(Discord.getServerChannel().getId()) && !user.isBot())
		{
			Role r = Role.MEMBER;
			// If member of PGF (mainly for BTS/outside PGF server)
			if (memberPGF != null)
			{
				r = Roles.getTop(
						Roles.getRolesById(memberPGF
								.getRoles().stream()
								.map(role -> role.getId())
								.collect(Collectors.toList()))
						);
			}
			
			content.replace("%", ""); // removes all "%"s from the message.
			
			// attempts to bring over formatting from discord.
			content = format(content, "\\*\\*\\*", "§l§o"); 
			content = format(content, "\\*\\*", "§l");
			content = format(content, "\\*", "§o");
			content = format(content, "__", "§n");
			
			// If not reply
			if(e.getMessage().getReferencedMessage() == null || e.getMessage().getReferencedMessage().getAuthor().isBot())
			{
				Bukkit.getServer().broadcastMessage(r.getColor()
						+ e.getMember().getEffectiveName()
						+ " §r§8-|| "
						+ OnAsyncPlayerChat.getMessageColor(e.getMember().getId())
						+ content);
				
				return;
				
			} else {
                Role replyRole = Role.MEMBER;
                
                if (memberPGF != null)
                {
                    replyRole = Roles.getTop(
                    		Roles.getRolesById(memberPGF
                    				.getRoles().stream()
                    				.map(role -> role.getId())
                    				.collect(Collectors.toList()))
                    		);
                }
                
                Bukkit.getServer().broadcastMessage(r.getColor()
                		+ e.getMember().getEffectiveName()
                		+ " replied to "
                		+ replyRole.getColor()
                		+ memberPGF.getEffectiveName()
                		+ " §r§8-|| "
                		+ OnAsyncPlayerChat.getMessageColor(e.getMember().getId())
                		+ content);
                
			}
		} 
		
		
		// message sent to the bot in DMs.
		if (e.getChannelType() == ChannelType.PRIVATE && !e.getAuthor().isBot()) {
			if (AccountLinking.linkAsk(content, user))
			{
				e.getChannel().sendMessage("Your account has been linked.").queue();
			} else
			{
				e.getChannel().sendMessage("Invalid code, please try generating a new code.").queue();
			}
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
