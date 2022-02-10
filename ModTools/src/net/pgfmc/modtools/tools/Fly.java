package net.pgfmc.modtools.tools;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
 /**
  * Fly Command
  * @author bk
  */
public class Fly implements CommandExecutor, Listener{
	
	/**
	 * Toggles flying state.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		PlayerData pd = PlayerData.getPlayerData(p);
		boolean fly = (boolean) Optional.ofNullable(pd.getData("fly")).orElse(false); // Gets "flying" from PlayerData, default to false if null, converts to boolean
		
		pd.setData("fly", !fly);
		p.setAllowFlight(!fly);
		p.setFlying(!fly);
		
		if (fly) { p.sendMessage("§cDisabled flight."); } else { p.sendMessage("§aEnabled flight!"); }
		
		return true;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		boolean fly = (boolean) Optional.ofNullable(PlayerData.getPlayerData(p).getData("fly")).orElse(false);
		
		if (fly)
		{
			p.setAllowFlight(true);
			p.setFlying(true);
			p.sendMessage("§aEnabled flight!");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerRespawnEvent e)
	{
		Player p = e.getPlayer();
		
		boolean fly = (boolean) Optional.ofNullable(PlayerData.getPlayerData(p).getData("fly")).orElse(false);
		
		if (fly)
		{
			p.setAllowFlight(true);
		}
	}
	
}
