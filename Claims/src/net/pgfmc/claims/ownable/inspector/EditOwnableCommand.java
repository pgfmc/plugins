package net.pgfmc.claims.ownable.inspector;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.claims.ownable.Ownable;
import net.pgfmc.claims.ownable.Ownable.Lock;
import net.pgfmc.claims.ownable.block.OwnableBlock;
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
public class EditOwnableCommand implements CommandExecutor {
	
	/**
	 * @version 4.0.2
	 * 
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) return true;
		if (((Player) sender).getGameMode() != GameMode.CREATIVE) return true;
		PlayerData pd = PlayerData.getPlayerData((Player) sender);
		
		Ownable cache = pd.getData("OwnableCache");
		if (cache == null) {
			sender.sendMessage("§cNo Ownable Selected!");
			return true;
		}
		
		String s = label;
		
		for (String sr : args) {
			s = s + " " + sr;
		}
		sender.sendMessage(s);
		
		if (cache instanceof OwnableBlock) {
			
			if (args == null || args.length > 1) {
				sender.sendMessage("§dAllowed types: §b'§alock§b'§d, §b'§aowner§b'");
				
			} else if ("lock".equals(args[0])) {
				
				if (args.length > 1) {
					Lock lock = Lock.valueOf(args[1]);
					
					if (lock != null) {
						cache.setLock(lock);
						sender.sendMessage("§aLock set to " + lock.toString());
						return true;
						
					} else {
						sender.sendMessage("§cPlease enter a valid Lock!");
						
					}
				} 
				sender.sendMessage("§cNo lock value input.");
				
			} else if ("owner".equals(args[0])) {
				
				if (args.length > 1) {
					PlayerData ope = PlayerData.getPlayerData(args[1]);
					if (ope != null) {
						cache.setOwner(ope);
						sender.sendMessage("§aOwner set to " + ope.getRankedName());
						return true;
					}
				}
				sender.sendMessage("§cPlease Enter a valid player!");
				
			} else {
				sender.sendMessage("§dAllowed types: §b'§alock§b'§d, §b'§aowner§b'");
			}
		} else {
			
			sender.sendMessage("§dSelect an Ownable in Inspector mode §b(/insp) §dfirst!");
		}
		return true;
	}
}
