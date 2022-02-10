package net.pgfmc.claims.ownable.inspector;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.claims.ownable.block.OwnableBlock;
import net.pgfmc.claims.ownable.block.table.ClaimsTable;
import net.pgfmc.core.util.Vector4;

public class ClaimTPCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) return true;
		if (((Player) sender).getGameMode() != GameMode.CREATIVE) return true;
		
		OwnableBlock ob = ClaimsTable.getRelevantClaim(new Vector4(((Player) sender).getLocation()));
		
		if (ob != null) {
			((Player) sender).teleport(ob.getLocation().toLocation());
			sender.sendMessage("§aTeleported to the active claim!");
		} else {
			sender.sendMessage("§6No claim in range.");
		}
		
		return true;
	}
	

}
