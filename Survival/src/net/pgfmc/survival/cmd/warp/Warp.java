package net.pgfmc.survival.cmd.warp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.core.util.commands.PlayerCommand;

public class Warp extends PlayerCommand {

	public Warp(String name) {
		super(name);
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
  
		List<String> list = new ArrayList<>();
		
		if (args.length == 1) {
			for (String s : WarpLogic.getWarpNames()) {
				if (s.startsWith(args[0])) {
					list.add(s);
				}
			}
		}
		
		return list;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		if (args.length == 0) {
			
			pd.sendMessage(ChatColor.RED + "Please enter a warp location.");
			return true;
		}
		
		String name = args[0].replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		
		Location loc = WarpLogic.getWarp(name);
		
		if (loc == null)
		{
			pd.sendMessage(ChatColor.RED + name  + " is not a warp");
			return true;
		}
		
		pd.sendMessage(ChatColor.GREEN + "Warping to " + ChatColor.GOLD + name + ChatColor.GREEN + " in 5 seconds!");
		
		new TimedTeleport(pd.getPlayer(), loc, 5, 40, true).setAct(v -> {
			pd.sendMessage(ChatColor.GREEN + "Poof!");
			pd.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
			if (pd.hasTag("afk")) pd.removeTag("afk");
		});
		
		
		
		return true;
	}

}
