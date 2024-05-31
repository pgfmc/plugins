package net.pgfmc.proxycore.bot.discord.slashcommands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.Logger;

public class LinkSlashCommand {
	
	private static final Map<String, UUID> linkCodes = new HashMap<>();
	
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
		
		if (!linkCodes.containsKey(inputCode))
		{
			e.getHook().sendMessage("Invalid link code.").queue();
			return;
		}
		
		final UUID uuid = linkCodes.get(inputCode);
		
		GlobalPlayerData.setData(uuid, "discord", user.getId());
		RoleManager.propogatePlayerRole(uuid);
		
		final Optional<Player> player = Main.plugin.proxy.getPlayer(uuid);
		
		if (player.isPresent())
		{
			Logger.log("Account linking: Successfully linked (Minecraft)" + player.get().getUsername() + " and (Discord)" + user.getName());
			
			e.getHook().sendMessage("Your account has been linked to " + player.get().getUsername() + ".").queue();
			
			player.get().sendMessage(Component.text("Your roles have been updated!").color(NamedTextColor.GREEN));
		} else
		{
			Logger.log("Account linking: Successfully linked (Minecraft) Offline Player and (Discord)" + user.getName());
			
			e.getHook().sendMessage("Your account has been linked.").queue();
		}
		
	}
	
	public static final void addLinkCode(final UUID uuid, final String code)
	{
		linkCodes.put(code, uuid);
		
		// This is here to prevent memory leaks
		if (linkCodes.size() > 50)
		{
			// Keep removing keys until the map size is smaller
			linkCodes.keySet().removeIf(key -> linkCodes.size() > 10); // Removing keys from the set removes the mapping from the map
		}
		
	}

}
