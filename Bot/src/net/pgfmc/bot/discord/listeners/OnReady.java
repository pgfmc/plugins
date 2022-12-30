package net.pgfmc.bot.discord.listeners;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.pgfmc.bot.Main;
import net.pgfmc.bot.discord.AutoRole;
import net.pgfmc.bot.discord.Discord;

public class OnReady extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent e) {		
		Bukkit.getLogger().warning("Discord Bot initialized!");
		
		// Don't run the code below if Machine isn't MAIN !!!!!!!!
		if (!Main.plugin.getConfig().getBoolean("enable-command")) return;
		
		Guild guild = Discord.getGuildPGF();
		
		AutoRole.giveAllMemberRole();
		
		guild.updateCommands().addCommands(
				Commands.slash("list", "Show who's online."),
				Commands.slash("link", "Link your Minecraft account")
								.addOption(OptionType.INTEGER, "code", "The link code from Minecraft.")
				).queue();
		
	}

}
