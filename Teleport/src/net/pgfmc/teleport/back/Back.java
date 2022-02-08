package net.pgfmc.teleport.back;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.teleportAPI.TimedTeleport;
import net.pgfmc.survival.cmd.Afk;

/**
 * Command to teleport the player to their last death location.
 * @author bk
 */
public class Back implements CommandExecutor, Listener {
	
	/**
	 * Teleports a player their back location if it exists
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) { return true; }
		
		Player p = (Player) sender;
		PlayerData pd = PlayerData.getPlayerData(p);
		
		Location dest = pd.getData("backLoc");
		if (dest == null)
		{
			p.sendMessage("§cYou do not have a back location.");
			return true;
		}
		
		p.sendMessage("§6You will be teleported in 5 seconds.");
		
		new TimedTeleport(p, dest, 5, 40, true).setAct(v -> {
			p.sendMessage("§aPoof!");
			p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0);
			if (Afk.isAfk(p)) { Afk.toggleAfk(p); }
		});
		
		
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
