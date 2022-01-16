package net.pgfmc.bot.functions;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.entities.Message;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.core.Mixins;

public class StartStopMessageDelete {
	
	public static void run(Message m)
	{
		if (m.getContentRaw().contains(Discord.START_MESSAGE)) {
			
			Main.action = x -> {
				m.getChannel().deleteMessageById(m.getId()).queue();
			};
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				
				@Override
				public void run()
				{
					Main.action.accept(null);
					Main.action = null;
				}
				
			}, 20 * 60);
		} else if (m.getContentRaw().contains(Discord.STOP_MESSAGE)) {
			
			
			FileConfiguration database = Mixins.getDatabase(Main.configPath);
			database.set("delete", m.getId());
			
			try {
				database.save(Mixins.getFile(Main.configPath));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
