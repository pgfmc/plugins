package net.pgfmc.core.playerdataAPI.cmd;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import net.pgfmc.core.cmd.base.CmdBase;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class PlayerDataSetCommand extends CmdBase {

	public PlayerDataSetCommand() {
		super("setplayerdata");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length == 0) {
			
			sender.sendMessage("§cPlease enter a player.");
			return true;
		}
		
		PlayerData pd = null;
			
		if (args.length >= 1) {
			
			pd = PlayerData.from(args[0]);
			if (pd == null) {
				sender.sendMessage("§cPlease enter a valid player.");
				return true;
				
			} else if (args.length == 1) {
				sender.sendMessage("§cPlease enter a key.");
				return true;
			}
		}
		
		String key = null;
		
		if (args.length >= 2) {
			
			key = args[1];
			if (args.length == 2) {
				sender.sendMessage("§cPlease enter a data value.");
				return true;
			}
		}
		
		String data = null;
		
		if (args.length >= 3) {
			
			data = args[2];
			Object obj = pd.getData(key);
			
			if (data == "null") {
				data = null;
			}
			
			if (obj instanceof String) {
				pd.setData(key, data);
				sender.sendMessage("§aSet §b" + key + " §ato §d" + data + "§a.");
				
			} else {
				sender.sendMessage("§cData wasn't a string, couldn't set.");
			}
		}
		
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		List<String> list = new LinkedList<>();
			
		if (args.length == 1) {
			
			for (PlayerData pd : PlayerData.getPlayerDataSet()) {
				String name = pd.getDisplayNameRaw();
				
				if (name.startsWith(args[0])) {
					list.add(name);
				}
			}
		} else 
		
		if (args.length == 2) {
			
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
