package net.pgfmc.core.bot.discord.listeners;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pgfmc.core.bot.discord.AutoRole;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.bot.discord.MakeSlashCommands;

public class OnReady extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent e) {
		Discord.JDA = e.getJDA();
		
		Bukkit.getLogger().warning("Discord Bot initialized!");
		
		if (!Discord.CHANNEL_SERVER.equals("784261883632681032")) return; // 784261883632681032 is the PGF #server
		
		new AutoRole();
		new MakeSlashCommands();
		
	}

}
