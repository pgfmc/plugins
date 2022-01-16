package net.pgfmc.bot.listeners;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.CoreMain.Machine;

public class OnReady implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {
		
		if (!(e instanceof ReadyEvent)) { return; }
			
		// auto delete stuff down below :\
		FileConfiguration database = Mixins.getDatabase(Main.configPath);
		
		String s = database.getString("channel");
		
		// sets the channel from the channel stored on config.yml.
		if (s != null) {
			Discord.SERVER_CHANNEL = s;
		} else {
			Discord.SERVER_CHANNEL = "771247931005206579";
			database.set("channel", "771247931005206579");
		}
		Discord.setChannel(Discord.JDA.getTextChannelById(Discord.SERVER_CHANNEL));
		
		if (database.getString("delete") != null) { // deletes the "server stopping" message.
			AuditableRestAction<Void> EEEE = Discord.getChannel().deleteMessageById(database.getString("delete"));
			
			try {
				EEEE.complete();
			} catch(Exception except) {
				System.out.println("Message delete failed");
				except.printStackTrace();
			}
		}
		database.set("delete", null);
		
		try {
			database.save(Mixins.getFile(Main.configPath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Discord Bot Initialized!");
		Discord.sendMessage(Discord.START_MESSAGE);
		
		if (CoreMain.machine == Machine.MAIN)
		{
			Discord.sendAlert(Discord.START_MESSAGE_EMBED);
		}
		
	}
	
	

}
