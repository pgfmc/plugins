package net.pgfmc.core.api.playerdata.cmd;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.CommandBase;

public class PlayerDataSetCommand extends CommandBase {

	public PlayerDataSetCommand() {
		super("setplayerdata");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length == 0) {
			
			sender.sendMessage("§cPlease enter a player.");
			return true;
		}
		
		if (args.length == 1)
		{
			sender.sendMessage("§cPlease enter a key.");
			return true;
		}
		
		@SuppressWarnings("deprecation")
		PlayerData pd = PlayerData.from(args[0]);
			
		if (pd == null) {
			sender.sendMessage(ChatColor.RED + "Player not found.");
			return true;
		}
		
		if (args.length == 2) {
			sender.sendMessage("§cPlease enter a data value.");
			return true;
		}
		
		String key = args[1];
		
		String data = args[2];
		Object obj = pd.getData(key);
		
		if (data == "null") {
			data = null;
		}
		
		if (!(obj instanceof String)) {
			sender.sendMessage("§cData wasn't a string, couldn't set.");
			return true;
		}
		
		pd.setData(key, data);
		sender.sendMessage("§aSet §b" + key + " §ato §d" + data + "§a.");
		
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
			
			@SuppressWarnings("deprecation")
			PlayerData pd = PlayerData.from(args[0]);
			if (pd == null) return list;
			
			for (String entry : pd.getAllData().keySet()) {
				if (entry.startsWith(args[1]) && pd.getData(entry) instanceof String) {
					list.add(entry);
				}
			}
		}
		
		return list;
	}
	
	
	
	
	
	
	

}
