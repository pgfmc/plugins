package net.pgfmc.survival.cmd.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.teleport.TimedTeleport;
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
			pd.sendMessage("§cPlease enter the name of a home.");
			return true;
		}
		
		HashMap<String, Location> homes = Homes.getHomes(pd);
		String name = args[0].toLowerCase();
		if (homes.containsKey(name))
		{
			pd.sendMessage("§aTeleporting to home §6" + name + "§a in 5 seconds!");
			
			new TimedTeleport(pd.getPlayer(), homes.get(name), 5, 40, true).setAct(v -> {
				pd.sendMessage("§aPoof!");
				pd.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
				if (pd.hasTag("afk")) pd.removeTag("afk");
			});;
		} else
		{
			pd.sendMessage("§aCould not find home §6" + name + "§a.");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
		}
		
		return true;
	}

}
