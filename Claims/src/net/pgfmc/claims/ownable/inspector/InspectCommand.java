package net.pgfmc.claims.ownable.inspector;

import java.util.List;

import org.bukkit.ChatColor;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Inspector Command
 * @author CrimsonDart
 * @version 6.0.0
 * @since 4.0.2
 */
public class InspectCommand extends PlayerCommand {
	
	public InspectCommand(String name) {
		super(name);
	}
	
	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
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
		
		boolean insp = pd.hasTag("inspector");
		
		
		if (insp) {
			pd.removeTag("inspector");
			pd.sendMessage(ChatColor.GOLD + "Disabled Inspector mode.");
		} else {
			pd.addTag("inspector");
			pd.sendMessage(ChatColor.GREEN + "Enabled Inspector mode.");
			pd.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "Break Blocks to show Claim information on the block.");
		}
		
		return insp;
	}
}
