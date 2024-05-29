package net.pgfmc.proxycore.bot;

import java.io.File;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.moandjiezana.toml.Toml;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.Mixins;

public class Bot {
	
	public Bot()
	{
		final File botConfig = Mixins.getFile(Path.of(Main.plugin.configDirectory + File.separator + "bot.toml"));
		final Toml toml = new Toml()
				.read(botConfig);
		
		final String token = toml.getString("token");
		Long alertChannel = toml.getLong("alert-channel");
		Long serverChannel = toml.getLong("server-channel");
		
		if (toml.isEmpty())
		{
			alertChannel = 938677080349093898L;
			serverChannel = 771247931005206579L;
			
			final Map<String, Object> tomlMap = toml.toMap();
			tomlMap.put("token", "PLACE-TOKEN-HERE");
			tomlMap.put("alert-channel", alertChannel);
			tomlMap.put("server-channel", serverChannel);
			
			Mixins.writeKeyValueToToml(botConfig, tomlMap);
			
		}
		
		if (!toml.contains("token"))
		{
			Logger.error("bot.toml does not contain a bot token.");
		}
		
		if (!toml.contains("alert-channel"))
		{
			Logger.error("bot.toml does not contain alert channel ID.");
		}
		
		if (!toml.contains("server-channel"))
		{
			Logger.error("bot.toml does not contain server channel ID.");
		}
		
		try {
			new Discord(token, alertChannel, serverChannel);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		final MessageEmbed startMessageEmbed = Discord.createServerEmbed("Server is starting...", "https://cdn.discordapp.com/emojis/905682398790959125.png?size=44", NamedTextColor.GREEN)
												.setTimestamp(OffsetDateTime.now())
											.build();
		
		Discord.sendServerMessageEmbed(startMessageEmbed).queue();
		
		new MessageHistory(Discord.getServerChannel()).retrievePast(20).queueAfter(1, TimeUnit.MINUTES, messages -> {
			messages.stream().filter(message -> !message.getEmbeds().isEmpty() && message.getAuthor().getId().equals("721949520728031232"))
			.forEach(message -> message.delete().queue());
		});
		
	}
	
	public static void shutdown()
	{
		final MessageEmbed stopMessageEmbed = Discord.createServerEmbed("Server is stopping..."
				, "https://cdn.discordapp.com/emojis/905683316844429312.png?size=44"
				, NamedTextColor.RED)
				.setTimestamp(OffsetDateTime.now())
		.build();
		
		// #.complete will block the thread
		// This makes sure the messages are sent before the bot shuts down
		Discord.sendServerMessageEmbed(stopMessageEmbed).complete();
		
		Discord.getJda().shutdown();
		
	}
	
}
