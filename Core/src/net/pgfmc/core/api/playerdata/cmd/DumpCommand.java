package net.pgfmc.core.api.playerdata.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.format.NamedTextColor;
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
			sender.sendMessage(NamedTextColor.RED + "Please enter a player.");
			return true;
		}
		
		final Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null)https://docs.papermc.io/adventure/text/
		{
			sender.sendMessage(NamedTextColor.RED + "Player not found.");
			return true;
		}
		
		PlayerData pd = PlayerData.from(player);
		
		for (Entry<String, Object> entry : pd.getAllData().entrySet()) {
			
			if (entry.getValue() == null) {
				sender.sendMessage(NamedTextColor.WHITE + "[" + NamedTextColor.AQUA + entry.getKey() + NamedTextColor.WHITE + "]: " + NamedTextColor.LIGHT_PURPLE + "null");
				
				continue;
			}
			
			sender.sendMessage(NamedTextColor.WHITE + "[" + NamedTextColor.AQUA + entry.getKey() + NamedTextColor.WHITE + "]: " + NamedTextColor.LIGHT_PURPLE + entry.getValue().toString());
		}
		
		// tag list
		
		sender.sendMessage(NamedTextColor.AQUA + "Listing all tags for " + pd.getRankedName());
		
		Set<String> tags = pd.getTags();
		int length = tags.size();
		
		int iit = 0;
		String list = String.valueOf(NamedTextColor.LIGHT_PURPLE);
		for (String tag : tags)
		{
			list = list + tag;
			
			if (length -1 == iit) {
				break;
			}
			
			list = list + NamedTextColor.WHITE + "," +  NamedTextColor.LIGHT_PURPLE;
			
			if (iit % 3 == 2) {
				list = list + "\n";
			}
		}
		
		if (length > 25) {
			list = list + "\n" + NamedTextColor.AQUA + "Player has " + NamedTextColor.LIGHT_PURPLE + String.valueOf(length) + " " + NamedTextColor.LIGHT_PURPLE + "tags.";
		}
		sender.sendMessage(list);
		
		return true;
	}
	
}
