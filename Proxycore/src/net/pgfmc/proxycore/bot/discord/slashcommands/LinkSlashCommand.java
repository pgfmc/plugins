package net.pgfmc.proxycore.bot.discord.slashcommands;

import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.commands.LinkCommand;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.Logger;

public class LinkSlashCommand {
	
	public LinkSlashCommand(final SlashCommandInteractionEvent e)
	{
		e.deferReply().queue(); // "Thinking..."
		
		final User user = e.getUser();
		final String inputCode = e.getOption("code").getAsString();
		
		if (inputCode.length() != 4)
		{
			e.getHook().sendMessage("Invalid link code.").queue();
			return;
		}
		
		Logger.debug("Account linking: Input code is " + inputCode);
		
		final Player player = LinkCommand.checkPlayerLinkCode(inputCode);
		
		if (player == null)
		{
			e.getHook().sendMessage("Invalid link code.").queue();
			return;
		}
		
		Logger.log("Account linking: Successfully linked (Minecraft)" + player.getUsername() + " and (Discord)" + user.getName());
		
		RoleManager.linkPlayerDiscord(player.getUniqueId(), user.getId());
		RoleManager.propogatePlayerRole(player.getUniqueId());
		
		player.sendMessage(Component.text("Your roles have been updated!").color(NamedTextColor.GREEN));
		
		e.getHook().sendMessage("Your account has been linked to " + player.getUsername() + ".").queue();
		
	}

}
