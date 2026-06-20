package net.pgfmc.core.listeners.minecraft;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		final Player player = e.getEntity();
		final PlayerData playerdata = PlayerData.from(player);
		TranslatableComponent deathMessage = (TranslatableComponent) e.deathMessage();

        Logger.warn(deathMessage.arguments().toString());

        deathMessage.color(NamedTextColor.GOLD);

        deathMessage.replaceText(
                TextReplacementConfig.builder()
                .match(player.getName())
                .replacement(playerdata.getRankedName())
                .build());



		
		final Entity causingEntity = e.getDamageSource().getCausingEntity();
		
		if (causingEntity instanceof Player)
		{
			final Player causingEntityPlayer = (Player) causingEntity;
			final PlayerData causingEntityPlayerPlayerdata = PlayerData.from(causingEntityPlayer);

            deathMessage.replaceText(
                    TextReplacementConfig.builder()
                    .match(causingEntityPlayer.getName())
                    .replacement(causingEntityPlayerPlayerdata.getRankedName())
                    .build());
		}

		PluginMessageType.MESSAGE.send(player, deathMessage);
		PluginMessageType.DISCORD_MESSAGE.send(playerdata.getPlayer(), "<:DEATH:907865162558636072> " + ((TranslatableComponent) deathMessage).fallback());
		
		e.deathMessage(null);
	}
}
