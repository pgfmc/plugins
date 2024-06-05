package net.pgfmc.core.listeners.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		final Player player = e.getEntity();
		final PlayerData playerdata = PlayerData.from(player);
		String deathMessage = ChatColor.GOLD + e.getDeathMessage()
				.replaceAll(player.getName(), ChatColor.RESET + playerdata.getRankedName() + ChatColor.RESET + ChatColor.GOLD);
		
		final Entity causingEntity = e.getDamageSource().getCausingEntity();
		
		if (causingEntity instanceof Player)
		{
			final Player causingEntityPlayer = (Player) causingEntity;
			final PlayerData causingEntityPlayerPlayerdata = PlayerData.from(causingEntityPlayer);
			
			if (causingEntityPlayerPlayerdata.isOnline())
			{
				deathMessage = deathMessage
						.replaceAll(causingEntityPlayer.getName(), ChatColor.RESET
																 + causingEntityPlayerPlayerdata.getRankedName()
																 + ChatColor.RESET + ChatColor.GOLD);
				
			}
			
		}

		PluginMessageType.MESSAGE.send(player, deathMessage);
		
		PluginMessageType.DISCORD_MESSAGE.send(playerdata.getPlayer(), "<:DEATH:907865162558636072> " + ChatColor.stripColor(deathMessage));
		
		e.setDeathMessage(null);
		
	}

}
