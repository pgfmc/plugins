package net.pgfmc.survival.cmd.menu;

import java.util.List;

import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.survival.menu.CommandMenuInventory;

/**
 * Opens up a gui with all information a player might need
 * @author bk
 *
 */
public class CommandMenu extends PlayerCommand {

	public CommandMenu(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return List.of();
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		Player p = pd.getPlayer();
		
		p.openInventory(new CommandMenuInventory(pd).getInventory());
		
		return true;
	}
	
}
