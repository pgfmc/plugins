package net.pgfmc.core.api.playerdata.cmd;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		
		final Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Please enter a valid player.");
			return true;
		}
		
		if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "Please enter an action (add, remove, list).");
			return true;
		}
		
		String action = args[1];
		
		if (!(action.equals("add") || action.equals("remove") || action.equals("list")))
		{
			sender.sendMessage(ChatColor.RED + "Please enter "
					+ ChatColor.LIGHT_PURPLE + "add" + ChatColor.RED + ", "
					+ ChatColor.LIGHT_PURPLE + "remove" + ChatColor.RED + ", or "
					+ ChatColor.LIGHT_PURPLE + "list" + ChatColor.RED + ".");
			
			return true;
		}
		
		if (args.length == 2 && (action.equals("add") || action.equals("remove")))
		{
			sender.sendMessage(ChatColor.RED + "Please enter a tag.");
			return true;
		}
		
		if ((args.length == 3 && action.equals("list")) || args.length > 3)
		{
			sender.sendMessage(ChatColor.RED + "Invalid syntax -> Usage: /<command> <player> <add | remove | list> [tag]");
			return true;
		}
		
		PlayerData pd = PlayerData.from(player);
		
		if (action.equals("list")) {
			
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Listing all tags for " + pd.getRankedName()
								+ ChatColor.LIGHT_PURPLE + "\n-> "
								+ ChatColor.GOLD + String.join(", ", pd.getTags())
								+ ChatColor.LIGHT_PURPLE + " <-");
			
			return true;
		}
		
		String tag = args[2];
		
		if (action.equals("add")) {
			if (pd.addTag(tag)) {
				sender.sendMessage(ChatColor.AQUA + "Added tag " + ChatColor.LIGHT_PURPLE + tag + " " + ChatColor.AQUA + "to " + ChatColor.RESET + pd.getRankedName() + ChatColor.AQUA + ".");
			} else {
				sender.sendMessage(pd.getRankedName() + ChatColor.AQUA + " already has that tag!");
			}
		} else
		
		if (action.equals("remove")) {
			if (pd.removeTag(tag)) {
				sender.sendMessage(ChatColor.RED + "Removed tag " + ChatColor.LIGHT_PURPLE + tag + " " + ChatColor.RED + "from " + ChatColor.RESET + pd.getRankedName() + ChatColor.RED + ".");
			} else {
				sender.sendMessage(pd.getRankedName() + ChatColor.RED + " did not have that tag.");
			}
		}
		
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
			
			String[] argumes = {"add", "remove", "list"};
			
			for (String arg : argumes) {
				if (arg.startsWith(args[1])) {
					list.add(arg);
				}
			}
		}
		
		if (args.length == 3) {
			if (args[1].equals("remove")) {
				
				final Player player = Bukkit.getPlayer(args[0]);
				
				if (player == null) return list;
				
				final PlayerData playerdata = PlayerData.from(player);
				
				for (String arg : playerdata.getTags()) {
					if (arg.startsWith(args[1])) {
						list.add(arg);
					}
				}
			}
		}
		
		return list;
	}
}
