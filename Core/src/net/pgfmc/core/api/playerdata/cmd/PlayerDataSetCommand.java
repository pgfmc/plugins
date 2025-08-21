package net.pgfmc.core.api.playerdata.cmd;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.CommandBase;

public class PlayerDataSetCommand extends CommandBase {

	public PlayerDataSetCommand() {
		super("setplayerdata");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length == 0) {
			
			sender.sendMessage(ChatColor.RED + "Please enter a player.");
			return true;
		}
		
		if (args.length == 1)
		{
			sender.sendMessage(ChatColor.RED + "Please enter a key.");
			return true;
		}
		
		final Player player = Bukkit.getPlayer(args[0]);
			
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Player not found.");
			return true;
		}
		
		if (args.length == 2) {
			sender.sendMessage(ChatColor.RED + "Please enter a data value.");
			return true;
		}
		
		PlayerData pd = PlayerData.from(player);
		
		String key = args[1];
		
		String data = args[2];
		Object obj = pd.getData(key);
		
		if (data == "null") {
			data = null;
		}
		
		if (!(obj instanceof String)) {
			sender.sendMessage(ChatColor.RED + "Data wasn't a string, couldn't set.");
			return true;
		}
		
		pd.setData(key, data);
		sender.sendMessage(ChatColor.GREEN + "Set " + ChatColor.AQUA + key + " " + ChatColor.GREEN + "to " + ChatColor.AQUA + data + ChatColor.GREEN + ".");
		
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		List<String> list = new LinkedList<>();
			
		if (args.length == 1) {
			
			for (PlayerData pd : PlayerData.getPlayerDataSet()) {
				String name = pd.getName();
				
				if (name.startsWith(args[0])) {
					list.add(name);
				}
			}
		} else 
		
		if (args.length == 2) {
			
			final Player player = Bukkit.getPlayer(args[0]);
			
			if (player == null) return list;
			
			PlayerData pd = PlayerData.from(player);
			
			for (String entry : pd.getAllData().keySet()) {
				if (entry.startsWith(args[1]) && pd.getData(entry) instanceof String) {
					list.add(entry);
				}
			}
		}
		
		return list;
	}
	
	
	
	
	
	
	

}
