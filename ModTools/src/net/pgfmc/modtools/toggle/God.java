package net.pgfmc.modtools.toggle;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * God command
 * @author bk
 *
 */
public class God implements CommandExecutor, Listener {
	
	/**
	 * Toggles God state.
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
		boolean god = (boolean) Optional.ofNullable(pd.getData("god")).orElse(false); // Gets "god" from PlayerData, default to false if null, converts to boolean
		
		pd.setData("god", !god);
		p.setInvulnerable(!god);
		
		if (god) { p.sendMessage("§cDisabled god mode."); } else { p.sendMessage("§aEnabled god mode!"); }
		
		
		return true;
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) { // disables god when a player joines the servere
		Player p = e.getPlayer();
		
		boolean god = (boolean) Optional.ofNullable(PlayerData.getPlayerData(p).getData("god")).orElse(false);
		
		if (god)
		{
			p.setInvulnerable(true);
			p.sendMessage("§aEnabled god mode!");
		}
		
		
	}

}