package net.pgfmc.modtools.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

public class Invsee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		final Player player = (Player) sender;
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.RED + "Please include a player: /invsee <player>");
			return true;
		}
		
		final Player targetPlayer = Bukkit.getPlayer(args[0]);
		
		if (targetPlayer == null)
		{
			player.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
			return true;
		}
		
		final PlayerData target = PlayerData.from(targetPlayer);
		
		if (args.length == 1)
		{
			player.sendMessage(ChatColor.GOLD + "Opening " + target.getRankedName() + ChatColor.GOLD + "'s Inventory");
			player.openInventory(targetPlayer.getInventory());
		} else if (args.length == 2 && args[1].equals("echest"))
		{
			player.sendMessage(ChatColor.GOLD + "Opening " + target.getRankedName() + ChatColor.GOLD + "'s Ender Chest");
			player.openInventory(targetPlayer.getEnderChest());
		}
		
		return true;
	}

}
