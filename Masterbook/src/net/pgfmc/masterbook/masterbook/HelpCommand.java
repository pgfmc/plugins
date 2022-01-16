package net.pgfmc.masterbook.masterbook;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Opens up a gui with all information a player might need
 * @author bk
 *
 */
public class HelpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { return true; }
		
		Player p = (Player) sender;
		p.closeInventory();
		p.openInventory(new CommandsMenu(PlayerData.getPlayerData(p)).getInventory());
		
		return true;
	}
}
