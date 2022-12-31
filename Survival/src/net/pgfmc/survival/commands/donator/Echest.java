package net.pgfmc.survival.commands.donator;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Opens an Ender Chest for the player.
 * @author bk
 *
 */
public class Echest implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { return true; }
		
		Player p = (Player) sender;
		
		p.openInventory(p.getEnderChest());
		p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 0);
		
		return true;
	}
}
