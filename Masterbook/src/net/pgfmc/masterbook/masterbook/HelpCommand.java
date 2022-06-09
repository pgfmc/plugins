package net.pgfmc.masterbook.masterbook;

import java.util.List;

import org.bukkit.entity.Player;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Opens up a gui with all information a player might need
 * @author bk
 *
 */
public class HelpCommand extends PlayerCommand {

	public HelpCommand(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		Player p = pd.getPlayer();
		p.closeInventory();
		p.openInventory(new CommandsMenu(PlayerData.from(p)).getInventory());
		
		return true;
	}
}
