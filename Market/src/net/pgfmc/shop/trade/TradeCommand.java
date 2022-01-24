package net.pgfmc.shop.trade;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class TradeCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player) || ((Player) sender).getGameMode() != GameMode.SURVIVAL)
		{
			sender.sendMessage("§cYou cannot execute this command."); // lol
			return true;
		}
		
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		
		if (pd == null) return true;
		
		// XXX ((Player) sender).openInventory(new MainScreen(pd).getInventory()); // opens the inventory
		
		return true;
	}
}
