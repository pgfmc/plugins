package net.pgfmc.modtools.cmd.toggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.modtools.Main;

 /**
  * Toggles invisibility.
  * @author bk
  *
  */
public class Vanish implements CommandExecutor, Listener {

	/**
	 * Toggles invisibility, you are silent while invisible
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		PlayerData pd = PlayerData.from(p);
		boolean vanish = pd.hasTag("vanish"); // 		
		
		//p.setInvisible(!vanish); // This is only so the user knows they're in invis mode
		
		if (vanish)
		{
			Bukkit.getOnlinePlayers().stream().forEach(pl -> pl.showPlayer(Main.plugin, p));
			pd.removeTag("vanish");
			p.sendMessage(ChatColor.RED + "Vanish off.");
			p.setInvisible(false);
			p.setSilent(false);
			
		} else {
			Bukkit.getOnlinePlayers().stream().filter(pl -> !pl.hasPermission("net.pgfmc.modtools.vanish.exempt")).forEach(pl -> pl.hidePlayer(Main.plugin, p));
			pd.addTag("vanish");
			p.sendMessage(ChatColor.GREEN + "Vanished!");
			p.setInvisible(true);
			p.setSilent(true);
			
		}
		
		return true;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		boolean vanish = PlayerData.from(p).hasTag("vanish");
		
		if (vanish)
		{
			p.setInvisible(true);
			p.setSilent(true);
			p.sendMessage(ChatColor.GREEN + "Vanished!");
			
		}
	}
}
