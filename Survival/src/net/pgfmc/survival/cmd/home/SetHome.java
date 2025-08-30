package net.pgfmc.survival.cmd.home;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Profanity;
import net.pgfmc.core.util.SoundEffect;
import net.pgfmc.core.util.commands.PlayerCommand;

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
			pd.sendMessage(ChatColor.RED + "Please enter a name.");
			return true;
		}

		HashMap<String, Location> homes = Homes.getHomes(pd);
		String homeName = String.join("_", args);
		homeName = homeName.toLowerCase().strip().replace(" ", "_");
		
		if (Profanity.hasProfanity(homeName))
		{
			pd.sendMessage(ChatColor.RED + "Please do not include profanity!");
			SoundEffect.ERROR.play(pd);
			return true;
		}
		
		if (homes.containsKey(homeName))
		{
			pd.sendMessage(ChatColor.RED + "You cannot have duplicate home names: " + ChatColor.GOLD + homeName);
			SoundEffect.ERROR.play(pd);
			return true;
		}
		
		if (pd.hasPermission("net.pgfmc.survival.home.donator") && homes.size() >= 5)
		{
			pd.sendMessage(ChatColor.RED + "You can only have up to 5 homes: " + ChatColor.GOLD + Homes.getNamedHomes(pd));
			SoundEffect.ERROR.play(pd);
			return true;
		} else if (!pd.hasPermission("net.pgfmc.survival.home.donator") && homes.size() >= 3)
		{
			pd.sendMessage(ChatColor.RED + "You can only have up to 3 homes: " + ChatColor.GOLD + Homes.getNamedHomes(pd));
			SoundEffect.ERROR.play(pd);
			return true;
		}
		
		homes.put(homeName, pd.getPlayer().getLocation());
		
		pd.sendMessage(ChatColor.GREEN + "Set home " + ChatColor.GOLD + homeName + ChatColor.GREEN + "!");
		SoundEffect.PING.play(pd);
		pd.setData("homes", homes).queue();
		
		
		return true;
	}
	
	public static void setHome(PlayerData playerdata, String homeName)
	{
		
		
	}	

}
