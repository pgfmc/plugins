package net.pgfmc.claims.ownable.inspector;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Inspector Command
 * @author CrimsonDart
 * @version 4.0.2
 * @since 4.0.2
 */
public class InspectCommand implements CommandExecutor {
	
	/**
	 * @since 4.0.2
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) return true;
		if (((Player) sender).getGameMode() != GameMode.CREATIVE) return true;
		
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		toggleInspector(pd);
		
		return true;
	}
	
	/**
	 * Method that toggles the Inspector mode 
	 * 
	 * @param pd
	 * @return
	 */
	public static boolean toggleInspector(PlayerData pd) {
		
		boolean insp = pd.getData("inspector");
		pd.setData("inspector", !insp);
		
		if (insp) {
			pd.sendMessage("§6Disabled Inspector mode.");
		} else {
			pd.sendMessage("§aEnabled Inspector mode.");
			pd.sendMessage("§o§7Break Blocks to show Claim information on the block.");
		}
		
		return insp;
	}
	
}
