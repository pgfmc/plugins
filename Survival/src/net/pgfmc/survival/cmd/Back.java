package net.pgfmc.survival.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.teleport.TimedTeleport;
import net.pgfmc.core.util.commands.PlayerCommand;

/**
 * Command to teleport the player to their last death location.
 * @author bk
 */
public class Back extends PlayerCommand {
	
	public Back(String name) {
		super(name);
	}
	
	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		
		Location dest = pd.getData("backLoc");
		if (dest == null)
		{
			pd.sendMessage(ChatColor.RED + "You do not have a back location.");
			return true;
		}
		
		pd.sendMessage(ChatColor.GOLD + "You will be teleported in 5 seconds.");
		
		new TimedTeleport(pd.getPlayer(), dest, 5, 40, true).setAct(v -> {
			pd.sendMessage(ChatColor.GREEN + "Poof!");
			pd.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
			
			if (pd.hasTag("afk")) pd.removeTag("afk");
			pd.setData("backLoc", null); // wipes back location, dont want this to be too op
			
		});
		
		return true;
	}

}
