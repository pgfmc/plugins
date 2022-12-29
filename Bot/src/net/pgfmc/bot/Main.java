package net.pgfmc.bot;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.pgfmc.bot.discord.Discord;
import net.pgfmc.bot.minecraft.cmd.LinkCommand;
import net.pgfmc.bot.minecraft.cmd.ReportCommand;
import net.pgfmc.bot.minecraft.cmd.UnlinkCommand;
import net.pgfmc.bot.minecraft.listeners.OnAsyncPlayerChat;
import net.pgfmc.bot.minecraft.listeners.OnPlayerAdvancementDone;
import net.pgfmc.bot.minecraft.listeners.OnPlayerDeath;
import net.pgfmc.bot.minecraft.listeners.OnPlayerJoin;
import net.pgfmc.bot.minecraft.listeners.OnPlayerQuit;
import net.pgfmc.bot.util.Colors;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static String configPath;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
		PlayerDataManager.setInit(pd -> {
			FileConfiguration config = pd.loadFile();
			
			pd.setData("Discord", config.getString("Discord"));
			
		});
		
		new Discord();
		
		
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerAdvancementDone(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
		
		getCommand("link").setExecutor(new LinkCommand());
		getCommand("unlink").setExecutor(new UnlinkCommand());
		getCommand("report").setExecutor(new ReportCommand());
		
		MessageEmbed startMessageEmbed = Discord.simpleServerEmbed("Server is starting...", "https://cdn.discordapp.com/emojis/905682398790959125.png?size=44", Colors.GREEN).build();
		
		Discord.sendEmbed(startMessageEmbed).queue();
		Discord.sendAlert(startMessageEmbed).queue();
		
		MessageHistory feed = new MessageHistory(Discord.getServerChannel());
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				// Gets the past 20 messages
				
				feed.retrievePast(20).queue();
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
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
	
	@Override
	public void onDisable()
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
	
	public static String getChannelID(String entry) {
		return plugin.getConfig().getString(entry);
	}
}
