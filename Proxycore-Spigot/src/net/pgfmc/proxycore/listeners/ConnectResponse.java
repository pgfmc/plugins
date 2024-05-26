package net.pgfmc.proxycore.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.pluginmessage.PluginMessage;
import net.pgfmc.proxycore.util.pluginmessage.PluginMessageType;

/**
 * Tells the player and console the result
 */
public final class ConnectResponse extends PluginMessage {
	
	public ConnectResponse() {
		super(PluginMessageType.CONNECT);
		
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
	public void onPluginMessageReceived(final Player sender, final List<String> args) {
		if (sender == null || !sender.isOnline())
		{
			Logger.warn("Received invalid player (offline) in subchannel: ConnectResponse");
			
			return;
		}
		
		final PlayerData playerdata = PlayerData.from(sender);
		final String attemptedServerName = args.get(1);
		final String resultString = args.get(2);
		final boolean result = Boolean.parseBoolean(resultString);
		
		if (result)
		{
			Logger.debug("Successfully connected " + sender.getName() + " to server: " + attemptedServerName);
			
			sender.sendMessage(ChatColor.GREEN + "Poof!");
			playerdata.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 1.0F);
		} else
		{
			Logger.warn("This is the value of <True/False>: " + resultString);
			Logger.warn("Failed to connect to server: " + attemptedServerName);
			
			sender.sendMessage(ChatColor.RED + "Failed to connect to " + attemptedServerName + ".");
			playerdata.playSound(Sound.ENTITY_VILLAGER_NO);
		}
		
	}

}
