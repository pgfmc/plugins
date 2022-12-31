package net.pgfmc.survival.teleport;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.cmd.base.PlayerCommand;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.teleportapi.TimedTeleport;

/**
 * Command to teleport the player to their last death location.
 * @author bk
 */
public class Back extends PlayerCommand implements Listener {
	
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
			pd.sendMessage("§cYou do not have a back location.");
			return true;
		}
		
		pd.sendMessage("§6You will be teleported in 5 seconds.");
		
		new TimedTeleport(pd.getPlayer(), dest, 5, 40, true).setAct(v -> {
			pd.sendMessage("§aPoof!");
			pd.playSound(Sound.ENTITY_ENDERMAN_TELEPORT);
			if (pd.hasTag("afk")) pd.removeTag("afk");
		});
		
		
		// added by crimson
		pd.setData("backLoc", null); // wipes back location, dont want this to be too op
		
		return true;
	}
	
	/**
	 * Sets the back location on death
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		PlayerData.setData(e.getEntity(), "backLoc", e.getEntity().getLocation());
	}

}
