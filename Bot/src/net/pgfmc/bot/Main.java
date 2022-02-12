package net.pgfmc.bot;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.entities.MessageHistory;
import net.pgfmc.bot.cmd.LinkCommand;
import net.pgfmc.bot.cmd.ReportCommand;
import net.pgfmc.bot.cmd.UnlinkCommand;
import net.pgfmc.bot.functions.StartStopMessage;
import net.pgfmc.bot.listeners.minecraft.OnAsyncPlayerChat;
import net.pgfmc.bot.listeners.minecraft.OnPlayerAdvancementDone;
import net.pgfmc.bot.listeners.minecraft.OnPlayerDeath;
import net.pgfmc.bot.listeners.minecraft.OnPlayerJoin;
import net.pgfmc.bot.listeners.minecraft.OnPlayerQuit;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static String configPath;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerAdvancementDone(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
		
		getCommand("link").setExecutor(new LinkCommand());
		getCommand("unlink").setExecutor(new UnlinkCommand());
		getCommand("report").setExecutor(new ReportCommand());
		
		// Tries to initialize discord integration
		try {
			Discord.initialize();
		} catch (LoginException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		StartStopMessage.enable();
		
		MessageHistory feed = new MessageHistory(Discord.getServerChannel());	
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				// Gets the past 20 messages
				
				feed.retrievePast(20).queue();
			}
		}, 20);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				StartStopMessage.deleteStartStopMessages(feed);
			}
		}, 60 * 20);
	}
	
	@Override
	public void onDisable()
	{
		StringBuilder builder = new StringBuilder();
		
		for (Player p : Bukkit.getServer().getOnlinePlayers())
		{
			if (p.hasPermission("pgf.admin.fake.leave"))
			{
				if (PlayerData.from(p).hasTag("fake-leave")) continue;
			}
			
			builder.append("<:LEAVE:905682349239463957> " + PlayerData.from(p).getDisplayName() + "\n");
		}
		
		if (!StartStopMessage.isDeleted)
		{
			StartStopMessage.deleteStartStopMessages(new MessageHistory(Discord.getServerChannel()));
		}
		
		Discord.sendMessage(builder.toString()).queue();
		
		StartStopMessage.disable();
		
		Discord.JDA.shutdown();
	}
	
	public static String getChannelID(String entry) {
		return plugin.getConfig().getString(entry);
	}
}
