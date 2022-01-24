package net.pgfmc.bot;

import java.io.File;
import java.util.Random;
import java.util.function.Consumer;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.bot.cmd.LinkCommand;
import net.pgfmc.bot.cmd.UnlinkCommand;
import net.pgfmc.bot.player.ChatEvents;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.CoreMain.Machine;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static String configPath;
	
	public static Consumer<Random> action;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		configPath = plugin.getDataFolder() + File.separator + "config.yml";
		
		getServer().getPluginManager().registerEvents(new ChatEvents(), this);
		
		getCommand("link").setExecutor(new LinkCommand());
		getCommand("unlink").setExecutor(new UnlinkCommand());
		
		// Tries to initialize discord integration
		try {
			Discord.initialize();
		} catch (LoginException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void onDisable()
	{
		String msg = "";
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			
			msg += "<:LEAVE:905682349239463957> " + p.getName() + "\n";
		}
		
		Discord.sendMessage(msg);
		Discord.sendMessage(Discord.STOP_MESSAGE);
		
		if (CoreMain.machine == Machine.MAIN)
		{
			Discord.sendAlert(Discord.STOP_MESSAGE_EMBED);
		}
		
		if (action != null) {
			action.accept(null);
		}
		
		Discord.JDA.shutdown();
	}

}
