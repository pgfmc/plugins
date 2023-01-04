package net.pgfmc.core.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

/**
 * Block a player from all interaction
 *
 * @author bk
 *
 */
public class Blocked implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		if (args.length == 0)
		{
			return false;
		}
		Player p1 = (Player) sender;
		Player p2 = Bukkit.getPlayer(args[0]);
		
		if (p2 == null)
		{
			p1.sendMessage("§cCould not find player §6§n" + args[0] + "§r§c.");
			return true;
		}
		
		ADD_BLOCKED(p1, p2);
		
		return true;
	}
	
	public static void ADD_BLOCKED(OfflinePlayer p1, OfflinePlayer p2)
	{		
		List<UUID> blockList = GET_BLOCKED(p1);
		
		blockList.add(p2.getUniqueId());
		
		PlayerData.from(p1).setData("blockList", blockList);
	}
	
	public static void REMOVE_BLOCKED(OfflinePlayer p1, OfflinePlayer p2)
	{
		List<UUID> blockList = GET_BLOCKED(p1);
		
		blockList.remove(p2.getUniqueId());
		
		PlayerData.from(p1).setData("blockList", blockList).queue();
	}
	
	@SuppressWarnings("unchecked")
	public static List<UUID> GET_BLOCKED(OfflinePlayer p)
	{
		return Optional.ofNullable(
				(List<UUID>) PlayerData.from(p).getData("blocked")
				).orElse(new ArrayList<UUID>());
	}
	
	public static boolean IS_BLOCKED(OfflinePlayer p1, OfflinePlayer p2)
	{
		return GET_BLOCKED(p1).contains(p2.getUniqueId());
	}
	
	

}
