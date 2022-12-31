package net.pgfmc.bot.discord.listeners;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.discord.AutoRole;
import net.pgfmc.bot.discord.Discord;

public class OnReady extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent e) {		
		Bukkit.getLogger().warning("Discord Bot initialized!");
		
		if (!Main.plugin.getConfig().getBoolean("enable-command")) return;
		if (!Discord.CHANNEL_SERVER.equals("784261883632681032")) return; // 784261883632681032 is the PGF #server
		// Don't run the code below if Machine isn't MAIN !!!!!!!!
		
		Guild guild = Discord.getGuildPGF();
		
		AutoRole.giveAllMemberRole();
		
		guild.updateCommands().addCommands(
				new CommandData("list", "Show who's online."),
				new CommandData("link", "Link your Minecraft account")
								.addOption(OptionType.INTEGER, "code", "The link code from Minecraft.", true)
				).queue();
		
	}

}
