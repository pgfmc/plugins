package net.pgfmc.bot.minecraft.commands;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class LinkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			
			return true;
		}
		
		PlayerData pd = PlayerData.from((OfflinePlayer) sender);
		
		if (pd.getData("Discord") != null)
		{
			pd.sendMessage(ChatColor.RED + "Your Discord has already been linked.");
			pd.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/unlink" + ChatColor.RED + " to unlink your account.");
			
			return true;
		}
		
		sender.sendMessage(ChatColor.GREEN + "Generating code...");
		
		String code = generateCode();
		pd.setData("linkCode", code);
		
		sender.sendMessage(ChatColor.GOLD + "Do " + ChatColor.WHITE 
							+ "/link " + code + ChatColor.GOLD
							+ " in any channel in the " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "PGF Discord\n" + ChatColor.RESET + ChatColor.GOLD + "to finish linking your account (" + ChatColor.BLUE + "discord.gg/zdxeREe" + ChatColor.GOLD + ")");
		
		return true;
	}
	
	/**
	 * Get a randomly generated 4 digit code that isn't taken
	 * @return The 4 digit unique code
	 */
	private String generateCode()
	{
		String code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
		Bukkit.getLogger().warning("Generating code: Code is " + code);
		// range is 1000 to 9999
		LinkedList<String> takenCodes = new LinkedList<>();
		
		PlayerData.getPlayerDataSet(pd -> pd.getData("linkCode") != null
										&& pd.getData("linkCode") != "")
									.forEach(pd -> takenCodes.add((String) pd.getData("linkCode")));
		
		Bukkit.getLogger().warning("Generating code: Taken codes are " + takenCodes);
		
		while (takenCodes.contains(code))
		{
			Bukkit.getLogger().warning("Generating code: Code taken, generating new code");
			code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
			Bukkit.getLogger().warning("Generating code: Code is " + code);
		}
		
		return code;
	}
	
}