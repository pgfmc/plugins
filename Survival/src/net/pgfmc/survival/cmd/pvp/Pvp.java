package net.pgfmc.survival.cmd.pvp;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class Pvp extends PlayerCommand {

	public Pvp() {
		super("pvp");
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		if (pd.hasTag("pvp")) {
			pd.removeTag("pvp");
			
			Bukkit.broadcastMessage(pd.getRankedName() + ChatColor.GRAY + " disabled PVP");
			
			PluginMessageType.DISCORD_MESSAGE.send(pd.getPlayer(), ChatColor.stripColor(":shield: " + pd.getRankedName() + " disabled PVP"));
			
			CoreMain.updatePlayerNameplate(pd);
			
		} else {
			pd.addTag("pvp");
			
			Bukkit.broadcastMessage(pd.getRankedName() + ChatColor.DARK_RED + " enabled PVP");
			
			PluginMessageType.DISCORD_MESSAGE.send(pd.getPlayer(), ChatColor.stripColor(":crossed_swords: " + pd.getRankedName() + " enabled PVP"));
			
			CoreMain.updatePlayerNameplate(pd);
		}
		
		return true;
	}
	
}
