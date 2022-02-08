package net.pgfmc.bot.functions;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.pgfmc.bot.Discord;

public class StartStopMessage {
	
	public static boolean isDeleted = false;
	
	public static void deleteStartStopMessages(MessageHistory feed)
	{		
		feed.getRetrievedHistory()
		.stream()
		.filter(m -> !m.getEmbeds().isEmpty() && m.getAuthor().getId().equals("721949520728031232"))
		.forEach(m -> m.delete().queue());
		
		isDeleted = true;
	}
	
	public static void enable() {
		MessageEmbed startMessageEmbed = Discord.simpleServerEmbed("Server is starting..."
				, "https://cdn.discordapp.com/emojis/905682398790959125.png?size=44"
				, Discord.GREEN)
		.build();
		
		Discord.sendEmbed(startMessageEmbed);
		Discord.sendAlert(startMessageEmbed);
		
	}
	
	public static void disable() {
		MessageEmbed stopMessageEmbed = Discord.simpleServerEmbed("Server is stopping..."
				, "https://cdn.discordapp.com/emojis/905683316844429312.png?size=44"
				, Discord.RED)
		.build();
		
		Discord.sendEmbed(stopMessageEmbed);
		Discord.sendAlert(stopMessageEmbed);
		
	}
	

}
