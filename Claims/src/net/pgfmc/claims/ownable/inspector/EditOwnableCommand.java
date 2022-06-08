package net.pgfmc.claims.ownable.inspector;

import java.util.List;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.core.cmd.base.CreativeOnly;
import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Command that allows data for Ownables to be changed.
 * can set the lock or owner of the ownable.
 * 
 * @author CrimsonDart
 * @version 4.0.2
 * @since 4.0.2
 *
 */
public class EditOwnableCommand extends PlayerCommand implements CreativeOnly {
	
	public EditOwnableCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(PlayerData pd, String label, String[] args) {
		
		
		Claim cache = pd.getData("OwnableCache");
		if (cache == null) {
			pd.sendMessage("§cNo Ownable Selected!");
			return true;
		}
		
		if (args == null || args.length > 1) {
			pd.sendMessage("§dAllowed types: §b'§alock§b'§d, §b'§aowner§b'");
			
		} else if ("owner".equals(args[0])) {
			
			if (args.length > 1) {
				PlayerData ope = PlayerData.from(args[1]);
				if (ope != null) {
					cache.setOwner(ope);
					pd.sendMessage("§aOwner set to " + ope.getRankedName());
					return true;
				}
			}
			pd.sendMessage("§cPlease Enter a valid player!");
		}
		return true;
	}

	@Override
	public List<String> tabComplete(PlayerData arg0, String arg1, String[] arg2) {
		return null;
	}
}
