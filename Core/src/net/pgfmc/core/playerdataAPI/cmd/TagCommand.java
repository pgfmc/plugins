package net.pgfmc.core.playerdataAPI.cmd;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import net.pgfmc.core.cmd.base.CmdBase;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class TagCommand extends CmdBase {

	public TagCommand() {
		super("tag");
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
			}
		}
		
		int action = 0;
		
		if (args.length >= 2) {
			
			String act = args[1];
			
			if (act.equals("add")) {
				action = 1;
			} else if (act.equals("remove")) {
				action = 2;
			} else if (act.equals("list")) {
				action = 3;
			} else {
				sender.sendMessage("§cPlease enter §dadd§c, §dremove §cor §dlist§c.");
				return true;
			}
		}
		
		if (action == 3) {
			
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
		
		if (!(args.length >= 3)) {
			sender.sendMessage("§cPlease enter a tag.");
			return true;
		}
		String tag = args[2];
		
		if (action == 1) {
			if (pd.addTag(tag)) {
				sender.sendMessage("§bAdded tag §d" + tag + " §bto §r" + pd.getRankedName() + "§b.");
			} else {
				sender.sendMessage(pd.getRankedName() + " §balready has that tag!");
			}
		} else
		
		if (action == 2) {
			if (pd.removeTag(tag)) {
				sender.sendMessage("§cRemoved tag §d" + tag + " §cfrom §r" + pd.getRankedName() + "§c.");
			} else {
				sender.sendMessage(pd.getRankedName() + " §cdid not have that tag.");
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
