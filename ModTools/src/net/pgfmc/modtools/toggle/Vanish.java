package net.pgfmc.modtools.toggle;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
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
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		PlayerData pd = PlayerData.getPlayerData(p);
		boolean vanish = (boolean) Optional.ofNullable(pd.getData("vanish")).orElse(false); // Gets "invis" from PlayerData, default to false if null, converts to boolean
		
		pd.setData("vanish", !vanish);
		p.setInvisible(!vanish); // This is only so the user knows they're in invis mode
		
		if (vanish)
		{
			Bukkit.getOnlinePlayers().stream().forEach(pl -> pl.showPlayer(Main.plugin, p));
			p.sendMessage("§cVanish off.");
			p.performCommand("fakejoin");
		} else
		{
			Bukkit.getOnlinePlayers().stream().filter(pl -> !pl.hasPermission("pgf.admin.vanish.exempt")).forEach(pl -> pl.hidePlayer(Main.plugin, p));
			p.sendMessage("§aVanished!");
			p.performCommand("fakeleave");
		}
		
		return true;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		boolean vanish = (boolean) Optional.ofNullable(PlayerData.getPlayerData(p).getData("vanish")).orElse(false);
		
		if (vanish)
		{
			p.setInvisible(true);
			p.setSilent(true);
			p.sendMessage("§aVanish on!");
		}
	}

}
