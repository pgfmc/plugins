package net.pgfmc.core.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Profanity;

public class NicknameCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (!(player.hasPermission("net.pgfmc.core.nick")))
		{
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}
		
		final PlayerData playerdata = PlayerData.from(player);
		
		if (args.length <= 0)
		{
			playerdata.setData("nickname", null).queue().send();
			playerdata.sendMessage(ChatColor.GOLD + "Nickname reset to " + playerdata.getRankedName() + ChatColor.GOLD + ".");
			
			return true;
		}
		
		final String nickname = String.join("", args)
				.strip()
				.replaceAll("[^A-Za-z0-9\\Q!@#&()-?\\E]", "");
		
		if (nickname.isBlank())
		{
			playerdata.sendMessage(ChatColor.RED + "Invalid nickname: Empty.");
			return true;
		}
		
		/*
		 * The nickname must be less than 21 characters
		 */
		if (nickname.length() > 21)
		{
			playerdata.sendMessage(ChatColor.RED + "Invalid nickname: Too long.");
			return true;
		}
		
		if (Profanity.hasProfanity(nickname))
		{
			playerdata.sendMessage(ChatColor.RED + "Invalid nickname: Contains profanity!");
			return true;
		}
		
		/*
		 * If the raw nickname is "off" or "reset" or the player's name
		 * then it will reset the nickname to Player.getName()
		 */
		if (nickname.equals("reset") || nickname.equals(player.getName()))
		{
			playerdata.setData("nickname", null).queue().send();
			playerdata.sendMessage(ChatColor.GOLD + "Nickname reset to " + playerdata.getRankedName() + ChatColor.GOLD + ".");
			
			return true;
		}
		
		playerdata.setData("nickname", nickname).queue().send();
		playerdata.sendMessage(ChatColor.GOLD + "Nickname changed to " + playerdata.getRankedName() + ChatColor.GOLD + ".");
		
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) return List.of();
		
		return List.of(sender.getName(), "reset");
	}

}