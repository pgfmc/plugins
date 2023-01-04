package net.pgfmc.core.bot;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.playerdata.PlayerDataManager;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.minecraft.cmd.LinkCommand;
import net.pgfmc.core.bot.minecraft.cmd.UnlinkCommand;
import net.pgfmc.core.bot.minecraft.listeners.OnAsyncPlayerChat;
import net.pgfmc.core.bot.minecraft.listeners.OnPlayerDeath;
import net.pgfmc.core.bot.minecraft.listeners.OnPlayerJoin;
import net.pgfmc.core.bot.minecraft.listeners.OnPlayerQuit;
import net.pgfmc.core.bot.util.Colors;

public class Bot {
	public static String configPath;
	
	public Bot()
	{
		CoreMain.plugin.saveDefaultConfig();
		CoreMain.plugin.reloadConfig();
		
		PlayerDataManager.setInit(pd -> {
			FileConfiguration config = pd.loadFile();
			
			pd.setData("Discord", config.getString("Discord"));
			
		});
		
		new Discord();
		
		Bukkit.getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), CoreMain.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), CoreMain.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), CoreMain.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerQuit(), CoreMain.plugin);
		
		CoreMain.plugin.getCommand("link").setExecutor(new LinkCommand());
		CoreMain.plugin.getCommand("unlink").setExecutor(new UnlinkCommand());
		
		MessageEmbed startMessageEmbed = Discord.simpleServerEmbed("Server is starting...", "https://cdn.discordapp.com/emojis/905682398790959125.png?size=44", Colors.GREEN).build();
		
		Discord.sendEmbed(startMessageEmbed).queue();
		Discord.sendAlert(startMessageEmbed).queue();
		
		MessageHistory feed = new MessageHistory(Discord.getServerChannel());
		Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() {
			@Override
			public void run() {
				// Gets the past 20 messages
				feed.retrievePast(20).queue();
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(CoreMain.plugin, new Runnable() {
					@Override
					public void run() {
						feed.getRetrievedHistory()
						.stream()
						.filter(m -> !m.getEmbeds().isEmpty() && m.getAuthor().getId().equals("721949520728031232"))
						.forEach(m -> m.delete().queue());
						
					}
					
				}, 60 * 20);
				
			}
			
		}, 20);
		
	}
	
	public void shutdown()
	{
		StringBuilder builder = new StringBuilder();
		
		Bukkit.getServer().getOnlinePlayers().stream()
		.forEach(player -> {
			builder.append("<:LEAVE:905682349239463957> " + PlayerData.from(player).getDisplayName() + "\n");
		});
		
		Discord.sendMessage(builder.toString()).queue();
		
		MessageEmbed stopMessageEmbed = Discord.simpleServerEmbed("Server is stopping..."
				, "https://cdn.discordapp.com/emojis/905683316844429312.png?size=44"
				, Colors.RED)
		.build();
		
		Discord.sendEmbed(stopMessageEmbed).queue();
		Discord.sendAlert(stopMessageEmbed).queue();
		
		Discord.JDA.shutdown();
		
	}
	
}
