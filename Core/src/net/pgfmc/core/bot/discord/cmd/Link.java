package net.pgfmc.core.bot.discord.cmd;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.roles.RoleManager;

public class Link {
	
	public Link(SlashCommandInteractionEvent e)
	{
		e.deferReply().queue(); // "Thinking..."
		
		User user = e.getUser();
		String input = (e.getOption("code").getAsString() + "0000").substring(0, 4);
		Bukkit.getLogger().warning("Account linking: Input code is " + input);
		
		if (input.equals("0000"))
		{
			e.getHook().sendMessage("Invalid link code.").queue();
			return;
		}
		
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
		RoleManager.updatePlayerRole(pdMatch);
		
		pdMatch.playSound(pdMatch.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
		pdMatch.sendMessage(ChatColor.GREEN + "Your roles have been updated!");
		e.getHook().sendMessage("Your account has been linked to " + pdMatch.getName() + ".").queue();
		
	}

}
