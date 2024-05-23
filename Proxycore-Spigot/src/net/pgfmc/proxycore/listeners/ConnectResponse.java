package net.pgfmc.proxycore.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.proxycore.PluginMessage;
import net.pgfmc.proxycore.util.Logger;

public class ConnectResponse extends PluginMessage {
	
	public ConnectResponse(PluginMessageType messenger) {
		super(messenger);
	}
	
	/**
	 * [ConnectReponse]
	 * 
	 * The ConnectResponse subchannel is used for responding to
	 * the Connect subchannel. It says if the connection attempt was successful or not.
	 * 
	 * PLUGIN MESSAGE FORM (pgf:main): ConnectResponse, <server name>, <true/false>
	 */
	@Override
	public void onPluginMessageReceived(final Player player, final List<String> args) {
		if (player == null || !player.isOnline())
		{
			Logger.warn("Received invalid player (offline) in subchannel: ConnectResponse");
			
			return;
		}
		
		final PlayerData playerdata = PlayerData.from(player);
		final String attemptedServerName = args.get(1);
		final String resultString = args.get(2);
		final boolean result = Boolean.parseBoolean(resultString);
		
		if (result)
		{
			Logger.debug("Successfully connected " + player.getName() + " to server: " + attemptedServerName);
			
			player.sendMessage(ChatColor.GREEN + "Poof!");
			playerdata.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 1.0F);
		} else
		{
			Logger.warn("This is the value of <True/False>: " + resultString);
			Logger.warn("Failed to connect to server: " + attemptedServerName);
			
			player.sendMessage(ChatColor.RED + "Failed to connect to " + attemptedServerName + ".");
			playerdata.playSound(Sound.ENTITY_VILLAGER_NO);
		}
		
	}

}
