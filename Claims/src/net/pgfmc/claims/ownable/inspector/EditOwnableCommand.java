package net.pgfmc.claims.ownable.inspector;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.pgfmc.claims.ownable.block.Claim;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

/**
 * Command that allows data for Ownables to be changed.
 * can set the lock or owner of the ownable.
 * 
 * @author CrimsonDart
 * @version 4.0.2
 * @since 4.0.2
 *
 */
public class EditOwnableCommand extends PlayerCommand {
	
	public EditOwnableCommand(String name) {
		super(name);
	}

	@Override
	public boolean execute(PlayerData pd, String label, String[] args) {
		
		
		Claim cache = pd.getData("OwnableCache");
		if (cache == null) {
			pd.sendMessage(ChatColor.RED + "No Ownable Selected!");
			return true;
		}
		
		if (args == null || args.length > 1) {
			pd.sendMessage(ChatColor.LIGHT_PURPLE + "Allowed types: " 
								+ ChatColor.AQUA + "'" + ChatColor.GREEN + "lock" + ChatColor.AQUA 
								+ "'" + ChatColor.LIGHT_PURPLE + ", " + ChatColor.AQUA + "'" 
								+ ChatColor.GREEN + "owner" + ChatColor.AQUA + "'"); // "&b'&alock&b'&d, &b'&aowner&b'" // "'lock', 'owner'"
			
		} else if ("owner".equals(args[0])) {
			
			if (args.length > 1) {
				final Player player = Bukkit.getPlayer(args[1]);
				
				if (player == null)
				{
					pd.sendMessage(ChatColor.RED + "Please Enter a valid player!");
					return true;
				}
				
				PlayerData ope = PlayerData.from(player);
				if (ope != null) {
					cache.setOwner(ope);
					pd.sendMessage(ChatColor.GREEN + "Owner set to " + ope.getRankedName());
					return true;
				}
			}
			pd.sendMessage(ChatColor.RED + "Please Enter a valid player!");
		}
		return true;
	}

	@Override
	public List<String> tabComplete(PlayerData arg0, String arg1, String[] arg2) {
		return null;
	}
}
