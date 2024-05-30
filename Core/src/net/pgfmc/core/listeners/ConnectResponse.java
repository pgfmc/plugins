package net.pgfmc.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.proxy.PluginMessage;
import net.pgfmc.core.util.proxy.PluginMessageType;

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
	public void onPluginMessageTypeReceived(final Player sender, ByteArrayDataInput in, final byte[] message)
	{
		in.readUTF();
		final PlayerData playerdata = PlayerData.from(sender);
		final String transferServerName = in.readUTF();
		final boolean result = in.readBoolean();
		
		if (result)
		{
			Logger.debug("Successfully connected " + sender.getName() + " to server: " + transferServerName);
			
			sender.sendMessage(ChatColor.GREEN + "Poof!");
			playerdata.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 1.0F);
		} else
		{
			Logger.warn("Failed to connect " + sender.getName() + " to server: " + transferServerName);
			
			sender.sendMessage(ChatColor.RED + "Failed to connect to " + transferServerName + ".");
			playerdata.playSound(Sound.ENTITY_VILLAGER_NO);
		}
		
	}

}
