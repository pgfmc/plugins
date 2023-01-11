package net.pgfmc.core.api.playerdata.cmd;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.CommandBase;

public class TagCommand extends CommandBase {

	public TagCommand() {
		super("tag");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please enter a player.");
			return true;
		}
		
		@SuppressWarnings("deprecation")
		PlayerData pd = PlayerData.from(args[0]);
		
		if (pd == null) {
			sender.sendMessage("§cPlease enter a valid player.");
			return true;
		}
		
		if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "Please enter an action (add, remove, list).");
			return true;
		}
		
		String action = args[1];
		
		if (args.length == 2 && (action.equals("add") || action.equals("remove")))
		{
			sender.sendMessage(ChatColor.RED + "Please enter a tag.");
			return true;
		}
		
		if (!action.equals("list"))
		{
			sender.sendMessage(ChatColor.RED + "Please enter "
					+ ChatColor.LIGHT_PURPLE + "add" + ChatColor.RED + ", "
					+ ChatColor.LIGHT_PURPLE + "remove " + ChatColor.RED + "or "
					+ ChatColor.LIGHT_PURPLE + "list" + ChatColor.RED + ".");
			
			return true;
		}
		
		if (action.equals("list")) {
			
			sender.sendMessage("§bListing all tags for " + pd.getRankedName());
			
			Set<String> tags = pd.getTags();
			int length = tags.size();
			
			int i = 0;
			String list = "§d";
			for (String tag : tags) {
				
				list = list + tag;
				
				if (length -1 == i) {
					break;
				}
				
				list = list + "§f,§d";
				
				if (i % 3 == 2) {
					list = list + "\n";
				}
			}
			
			if (length > 25) {
				list = list + "\n§bPlayer has §d" + String.valueOf(length) + " §dtags.";
			}
			sender.sendMessage(list);
			return true;
		}
		
		String tag = args[2];
		
		if (action.equals("add")) {
			if (pd.addTag(tag)) {
				sender.sendMessage("§bAdded tag §d" + tag + " §bto §r" + pd.getRankedName() + "§b.");
			} else {
				sender.sendMessage(pd.getRankedName() + " §balready has that tag!");
			}
		} else
		
		if (action.equals("remove")) {
			if (pd.removeTag(tag)) {
				sender.sendMessage("§cRemoved tag §d" + tag + " §cfrom §r" + pd.getRankedName() + "§c.");
			} else {
				sender.sendMessage(pd.getRankedName() + " §cdid not have that tag.");
			}
		}
		
		return true;
	}

	@SuppressWarnings("deprecation")
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
			
			String[] argumes = {"add", "remove", "list"};
			
			for (String arg : argumes) {
				if (arg.startsWith(args[1])) {
					list.add(arg);
				}
			}
		}
		
		if (args.length == 3) {
			if (args[1].equals("remove")) {
				for (String arg : PlayerData.from(args[0]).getTags()) {
					if (arg.startsWith(args[1])) {
						list.add(arg);
					}
				}
			}
		}
		
		return list;
	}
}
