package net.pgfmc.core.bot;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.util.Colors;

public class Bot {
	public static String configPath;
	
	public Bot()
	{
		try {
			new Discord();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		MessageEmbed startMessageEmbed = Discord.simpleServerEmbed("Server is starting...", "https://cdn.discordapp.com/emojis/905682398790959125.png?size=44", Colors.GREEN)
												.setTimestamp(OffsetDateTime.now())
											.build();
		
		Discord.sendEmbed(startMessageEmbed).queue();
		Discord.sendAlert(startMessageEmbed).queue();
		
		new MessageHistory(Discord.getServerChannel()).retrievePast(20).queueAfter(1, TimeUnit.MINUTES, messages -> {
			messages.stream().filter(message -> !message.getEmbeds().isEmpty() && message.getAuthor().getId().equals("721949520728031232"))
			.forEach(message -> message.delete().queue());
		});
		
	}
	
	public static void shutdown()
	{
		StringBuilder builder = new StringBuilder();
		
		Bukkit.getServer().getOnlinePlayers().stream()
		.forEach(player -> {
			builder.append("<:LEAVE:905682349239463957> " + ChatColor.stripColor(PlayerData.from(player).getRankedName()) + "\n");
		});
		
		if (!builder.isEmpty()) Discord.sendMessage(builder.toString()).queue();
		
		
		MessageEmbed stopMessageEmbed = Discord.simpleServerEmbed("Server is stopping..."
				, "https://cdn.discordapp.com/emojis/905683316844429312.png?size=44"
				, Colors.RED)
				.setTimestamp(OffsetDateTime.now())
		.build();
		
		// #.complete will block the thread
		// This makes sure the messages are sent before the bot shuts down
		Discord.sendEmbed(stopMessageEmbed).complete();
		Discord.sendAlert(stopMessageEmbed).complete();
		
		Discord.JDA.shutdown();
		
	}
	
}
