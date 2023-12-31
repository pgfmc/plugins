package net.pgfmc.survival.cmd.pvp;

import java.util.List;

import org.bukkit.ChatColor;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.bot.discord.Discord;
import net.pgfmc.core.util.ServerMessage;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.core.util.roles.PGFRoles;

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
			
			ServerMessage.sendServerMessage(pd.getRankedName() + ChatColor.GREEN + " disabled PVP");
			Discord.sendMessage(ChatColor.stripColor(":shield: " + pd.getRankedName() + " disabled PVP")).queue();;
			
			PGFRoles.updatePlayerNameplate(pd);
			
		} else {
			pd.addTag("pvp");
			
			ServerMessage.sendServerMessage(pd.getRankedName() + ChatColor.RED + " enabled PVP");
			Discord.sendMessage(ChatColor.stripColor(":crossed_swords: " + pd.getRankedName() + " enabled PVP")).queue();;
			
			PGFRoles.updatePlayerNameplate(pd);
		}
		
		
		return true;
	}
	
}
