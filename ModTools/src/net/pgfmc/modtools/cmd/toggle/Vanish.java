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
		boolean vanish = pd.hasTag("vanish"); // Gets "invis" from PlayerData, default to false if null, converts to boolean
		
		
		p.setInvisible(!vanish); // This is only so the user knows they're in invis mode
		
		if (vanish)
		{
			Bukkit.getOnlinePlayers().stream().forEach(pl -> pl.showPlayer(Main.plugin, p));
			p.sendMessage(ChatColor.RED + "Vanish off.");
			p.performCommand("fakejoin");
			pd.removeTag("vanish");
			
			
		} else {
			Bukkit.getOnlinePlayers().stream().filter(pl -> !pl.hasPermission("pgf.admin.vanish.exempt")).forEach(pl -> pl.hidePlayer(Main.plugin, p));
			p.sendMessage(ChatColor.GREEN + "Vanished!");
			p.performCommand("fakeleave");
			pd.addTag("vanish");
			
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
			p.sendMessage(ChatColor.GREEN + "aVanish on!");
			
		}
		
	}
	
}
