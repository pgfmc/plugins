package net.pgfmc.bot.listeners;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.pgfmc.bot.Discord;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.functions.AutoRole;

public class OnReady implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {		
		if (!(e instanceof ReadyEvent)) { return; }
		Bukkit.getLogger().warning("Discord Bot Initialized!");
		
		// Don't run the code below if Machine isn't MAIN !!!!!!!!
		if (!Main.plugin.getConfig().getBoolean("enable-command")) return;
		
		Guild guild = Discord.getGuildPGF();
		
		new AutoRole().maintainRole();
		
		guild.upsertCommand(new CommandData("list", "Show who's online.")).queue();
		
	}

}
