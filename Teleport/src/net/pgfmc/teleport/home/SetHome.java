package net.pgfmc.teleport.home;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import net.pgfmc.core.chat.Profanity;
import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class SetHome extends PlayerCommand {

	public SetHome(String name) {
		super(name);
	}
	
	
	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (args.length == 0)
		{
			pd.sendMessage("§cPlease enter a name.");
			return true;
		}
		
		
		setHome(pd, String.join("_", args), null);
		return true;
	}
	
	public static void setHome(PlayerData pd, String name, Location loc)
	{
		HashMap<String, Location> homes = Homes.getHomes(pd);
		
		name = name.toLowerCase().strip().replace(" ", "_");
		
		if (Profanity.hasProfanity(name))
		{
			pd.sendMessage(ChatColor.RED + "Please do not include profanity!");
			return;
		}
		
		if (homes.containsKey(name))
		{
			pd.sendMessage("§cYou cannot have duplicate home names: §6" + name);
			return;
		}
		
		if (pd.hasPermission("pgf.cmd.donator.home") && homes.size() >= 5)
		{
			pd.sendMessage("§cYou can only have up to 5 homes: §6" + Homes.getNamedHomes(pd));
			return;
		} else if (!pd.hasPermission("pgf.cmd.donator.home") && homes.size() >= 3)
		{
			pd.sendMessage("§cYou can only have up to 3 homes: §6" + Homes.getNamedHomes(pd));
			return;
		}
		
		if (loc != null)
		{
			homes.put(name, loc);
		} else
		{
			homes.put(name, pd.getPlayer().getLocation());
		}
		
		pd.sendMessage("§aSet home §6" + name + "§a!");
		pd.setData("homes", homes).queue();
	}

	

}
