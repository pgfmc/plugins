package net.pgfmc.survival.masterbook.cmd;

import java.util.List;

import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.survival.masterbook.inv.MasterbookInventory;

/**
 * Opens up a gui with all information a player might need
 * @author bk
 *
 */
public class Masterbook extends PlayerCommand {

	public Masterbook(String name) {
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
		p.openInventory(new MasterbookInventory(PlayerData.from(p)).getInventory());
		
		return true;
	}
	
}
