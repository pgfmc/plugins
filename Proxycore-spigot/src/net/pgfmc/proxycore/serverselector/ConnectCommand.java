package net.pgfmc.proxycore.serverselector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Lang;

/**
 * ConnectCommand is used to connect to another server.
 * 
 * It opens a inventory containing available servers to the player.
 * 
 * For a server to display in the inventory, the player needs permission
 * to connect to it.
 */
public final class ConnectCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
		{
			sender.sendMessage(Lang.COMMAND_PLAYER_REQUIRED.getLang());
			return true;
		}
		
		final Player player = (Player) sender;
		
		
		if (!player.hasPermission("net.pgfmc.cmd.connect"))
		{
			player.sendMessage(Lang.PERMISSION_DENIED.getLang());
			return true;
		}
		
		player.openInventory(new ServerSelectorInventory(PlayerData.from(player)).getInventory());
		
		return true;
	}
	
}
