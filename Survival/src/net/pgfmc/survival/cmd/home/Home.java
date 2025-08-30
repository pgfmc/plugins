package net.pgfmc.survival.cmd.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.core.util.SoundEffect;
import net.pgfmc.core.util.commands.PlayerCommand;

public class Home extends PlayerCommand {

	public Home(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		
		List<String> list = new ArrayList<>();
		
		if (args.length == 0) return null;
		if (args.length == 1) {
			
			for (String s : Homes.getNamedHomes(pd)) {
				if (s.startsWith(args[0])) {
					list.add(s);
				}
			}
		}
		return list;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		if (args.length == 0)
		{
			pd.sendMessage(ChatColor.RED + "Please enter the name of a home.");
			return true;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(pd);
		String name = args[0].toLowerCase();
		if (homes.containsKey(name))
		{
			pd.sendMessage(ChatColor.GREEN + "Teleporting to home " + ChatColor.GOLD + name + ChatColor.GREEN + " in 5 seconds!");
			
			new TimedTeleport(pd.getPlayer(), homes.get(name), 5, 40, true).setAct(v -> {
				pd.sendMessage(ChatColor.GREEN + "Poof!");
				SoundEffect.TELEPORT.play(pd);
				
				if (pd.hasTag("afk")) pd.removeTag("afk");
				
			});;
		} else
		{
			pd.sendMessage(ChatColor.GREEN + "Could not find home " + ChatColor.GOLD + name + ChatColor.GREEN + ".");
			SoundEffect.ERROR.play(pd);
		}
		
		return true;
	}

}
