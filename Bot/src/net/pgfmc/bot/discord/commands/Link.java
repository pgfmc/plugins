package net.pgfmc.bot.discord.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Link {
	
	public Link(SlashCommandInteractionEvent e)
	{
		e.deferReply().queue(); // "Thinking..."
		
		User user = e.getUser();
		String input = e.getOption("code").getAsString();
		Bukkit.getLogger().warning("Account linking: Input code is " + input);
		
		PlayerData pdMatch = null; // the playerdata that has the code match.
		
		List<PlayerData> playerDataMatches = PlayerData.getPlayerDataSet(pd -> pd.getData("linkCode") != null 
																				&& pd.getData("linkCode").equals(input)
																				&& pd.getData("Discord") == null)
																		.stream().collect(Collectors.toList());
		
		if (playerDataMatches.isEmpty())
		{
			e.getHook().sendMessage("Invalid link code.").queue();
			return;
		}
		
		pdMatch = playerDataMatches.get(0);
		pdMatch.setData("linkCode", null);
		
		Bukkit.getLogger().warning("Account linking: Successfully linked (Minecraft)" + pdMatch.getName() + " and (Discord)" + user.getName());
		
		pdMatch.setData("Discord", user.getId()).queue();
		Roles.setRoles(pdMatch);
		
		pdMatch.sendMessage(ChatColor.GREEN + "Your roles have been updated!");
		e.getHook().sendMessage("Your account has been linked to " + pdMatch.getName() + ".").queue();
	}

}
