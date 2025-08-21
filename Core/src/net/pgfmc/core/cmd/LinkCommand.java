package net.pgfmc.core.cmd;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class LinkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			
			return true;
		}
		
		final PlayerData playerdata = PlayerData.from((OfflinePlayer) sender);
		
		if (playerdata.getData("discord") != null)
		{
			playerdata.sendMessage(ChatColor.RED + "Your Discord has already been linked.");
			playerdata.sendMessage(ChatColor.RED + "Use " + ChatColor.WHITE + "/unlink" + ChatColor.RED + " to unlink your account.");
			
			return true;
		}
		
		sender.sendMessage(ChatColor.GREEN + "Generating code...");
		
		final String code = generateCode();
		
		playerdata.setData("linkCode", code);
		
		//final String secret = Encryption.encrypt(code);
		
		PluginMessageType.LINK_CODE.send(playerdata.getPlayer(), code);
		
		sender.sendMessage(ChatColor.GOLD + "Do "
					     + ChatColor.WHITE + "/link " + code
					     + ChatColor.GOLD + " in any channel in the "
					     + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "PGF Discord\n"
					     + ChatColor.RESET + ChatColor.GOLD + "to finish linking your account ("
					     + ChatColor.BLUE + "discord.gg/zdxeREe"
					     + ChatColor.GOLD + ")");
		
		return true;
	}
	
	/**
	 * Get a randomly generated 4 digit code that isn't taken
	 * @return The 4 digit unique code
	 */
	private String generateCode()
	{
		String code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
		
		// range is 1000 to 9999
		final LinkedList<String> takenCodes = new LinkedList<>();
		
		PlayerData.getPlayerDataSet(playerdata -> playerdata.getData("linkCode") != null && playerdata.getData("linkCode") != "")
									.forEach(pd -> takenCodes.add((String) pd.getData("linkCode")));
		
		while (takenCodes.contains(code))
		{
			code = String.valueOf(new Random().nextInt(9999 - 1000) + 1000);
		}
		
		return code;
	}
	
}
