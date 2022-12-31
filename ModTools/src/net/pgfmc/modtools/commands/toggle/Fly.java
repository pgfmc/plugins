package net.pgfmc.modtools.commands.toggle;

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
		
		PlayerData pd = PlayerData.from(p);
		
		
		boolean fly = pd.hasTag("fly"); // Gets "flying" from PlayerData, default to false if null, converts to boolean
		
		
		if (fly) {
			pd.removeTag("fly");
			p.sendMessage("§cDisabled flight.");
		} else {
			pd.addTag("fly");
			p.sendMessage("§aEnabled flight!");
		}
		
		p.setAllowFlight(!fly);
		p.setFlying(!fly);
		
		return true;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		boolean fly = PlayerData.from(p).hasTag("fly");
		
		if (fly)
		{
			p.setAllowFlight(true);
			p.setFlying(true);
			p.sendMessage("§aEnabled flight!");
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		Player p = e.getPlayer();
		
		boolean fly = PlayerData.from(p).hasTag("fly");
		
		if (fly)
		{
			p.setAllowFlight(true);
		}
	}
	
}
