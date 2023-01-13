package net.pgfmc.core.bot.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class MakeSlashCommands {
	
	public MakeSlashCommands()
	{
		Guild guild = Discord.getGuildPGF();
		
		guild.updateCommands().addCommands(
				Commands.slash("list", "Show who's online."),
				Commands.slash("link", "Link your Minecraft account")
						.addOption(OptionType.INTEGER, "code", "The link code from Minecraft.", true))
				.queue();
		
	}

}
