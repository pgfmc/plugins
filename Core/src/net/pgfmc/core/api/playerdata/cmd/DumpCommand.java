package net.pgfmc.core.api.playerdata.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.CommandBase;

public class DumpCommand extends CommandBase {

	public DumpCommand() {
		super("dump");
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) {
			for (PlayerData pd : PlayerData.getPlayerDataSet()) {
				list.add(pd.getName());
			}
			
		} else if (args.length == 1) {
			
			for (PlayerData pd : PlayerData.getPlayerDataSet()) {
				String arg = pd.getName();
				if (arg.startsWith(args[0])) {
					list.add(arg);
				}
			}
		}
		
		return list;
	}
	
	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please enter a player.");
			return true;
		}
		
		@SuppressWarnings("deprecation")
		PlayerData pd = PlayerData.from(args[0]);
		
		if (pd == null)
		{
			sender.sendMessage(ChatColor.RED + "Player not found.");
			return true;
		}
		
		for (Entry<String, Object> entry : pd.getAllData().entrySet()) {
			
			if (entry.getValue() == null) {
				sender.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + entry.getKey() + ChatColor.WHITE + "]: " + ChatColor.LIGHT_PURPLE + "null");
				
				continue;
			}
			
			sender.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + entry.getKey() + ChatColor.WHITE + "]: " + ChatColor.LIGHT_PURPLE + entry.getValue().toString());
		}
		
		// tag list
		
		sender.sendMessage(ChatColor.AQUA + "Listing all tags for " + pd.getRankedName());
		
		Set<String> tags = pd.getTags();
		int length = tags.size();
		
		int iit = 0;
		String list = String.valueOf(ChatColor.LIGHT_PURPLE);
		for (String tag : tags)
		{
			list = list + tag;
			
			if (length -1 == iit) {
				break;
			}
			
			list = list + ChatColor.WHITE + "," +  ChatColor.LIGHT_PURPLE;
			
			if (iit % 3 == 2) {
				list = list + "\n";
			}
		}
		
		if (length > 25) {
			list = list + "\n" + ChatColor.AQUA + "Player has " + ChatColor.LIGHT_PURPLE + String.valueOf(length) + " " + ChatColor.LIGHT_PURPLE + "tags.";
		}
		sender.sendMessage(list);
		
		return true;
	}
	
}
