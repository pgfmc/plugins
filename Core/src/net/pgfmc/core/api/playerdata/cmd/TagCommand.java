package net.pgfmc.core.api.playerdata.cmd;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.CommandBase;

public class TagCommand extends CommandBase {

	public TagCommand() {
		super("tag");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage(NamedTextColor.RED + "Please enter a player.");
			return true;
		}
		
		final Player player = Bukkit.getPlayer(args[0]);
		
		if (player == null) {
			sender.sendMessage(NamedTextColor.RED + "Please enter a valid player.");
			return true;
		}
		
		if (args.length == 1) {
			sender.sendMessage(NamedTextColor.RED + "Please enter an action (add, remove, list).");
			return true;
		}
		
		String action = args[1];
		
		if (!(action.equals("add") || action.equals("remove") || action.equals("list")))
		{
			sender.sendMessage(NamedTextColor.RED + "Please enter "
					+ NamedTextColor.LIGHT_PURPLE + "add" + NamedTextColor.RED + ", "
					+ NamedTextColor.LIGHT_PURPLE + "remove" + NamedTextColor.RED + ", or "
					+ NamedTextColor.LIGHT_PURPLE + "list" + NamedTextColor.RED + ".");
			
			return true;
		}
		
		if (args.length == 2 && (action.equals("add") || action.equals("remove")))
		{
			sender.sendMessage(NamedTextColor.RED + "Please enter a tag.");
			return true;
		}
		
		if ((args.length == 3 && action.equals("list")) || args.length > 3)
		{
			sender.sendMessage(NamedTextColor.RED + "Invalid syntax -> Usage: /<command> <player> <add | remove | list> [tag]");
			return true;
		}
		
		PlayerData pd = PlayerData.from(player);
		
		if (action.equals("list")) {
			
			sender.sendMessage(NamedTextColor.LIGHT_PURPLE + "Listing all tags for " + pd.getRankedName()
								+ NamedTextColor.LIGHT_PURPLE + "\n-> "
								+ NamedTextColor.GOLD + String.join(", ", pd.getTags())
								+ NamedTextColor.LIGHT_PURPLE + " <-");
			
			return true;
		}
		
		String tag = args[2];
		
		if (action.equals("add")) {
			if (pd.addTag(tag)) {
				sender.sendMessage(
                        Component.text("Added tag ", NamedTextColor.AQUA)
                        .append(Component.text(tag + " ", NamedTextColor.LIGHT_PURPLE))
                        .append(Component.text("to ", NamedTextColor.AQUA))
                        .append(pd.getRankedName())
                        .append(Component.text(".", NamedTextColor.AQUA)));
			} else {
                sender.sendMessage(pd.getRankedName().append(Component.text(" already has that tag!", NamedTextColor.AQUA)));
			}
		} else
		
		if (action.equals("remove")) {

			if (pd.removeTag(tag)) {
				sender.sendMessage(
                        Component.text("Removed tag ", NamedTextColor.RED)
                        .append(Component.text(tag + " ", NamedTextColor.LIGHT_PURPLE))
                        .append(Component.text("from ", NamedTextColor.RED))
                        .append(pd.getRankedName())
                        .append(Component.text(".", NamedTextColor.RED)));
			} else {
                sender.sendMessage(pd.getRankedName().append(Component.text(" did not have that tag.", NamedTextColor.RED)));
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
