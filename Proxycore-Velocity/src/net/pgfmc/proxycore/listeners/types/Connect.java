package net.pgfmc.proxycore.listeners.types;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class Connect extends PluginMessage {

	public Connect(PluginMessageType type) {
		super(type);
		
	}
	
	/**
	 * The Connect subchannel is used for sending this player to the specified server.
	 * The response says if the connection attempt was successful or not.
	 * 
	 * PLUGIN MESSAGE FORM: Connect, <server name>
	 * RESPONSE PLUGIN MESSAGE FORM: Connect, <server name>, <true/false>
	 */
	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, List<String> response) {
		// The name of the requested server transfer
		final String serverName = response.get(1);
		
		if (serverName != null && !serverName.equalsIgnoreCase(connection.getServerInfo().getName()))
		{
			Logger.debug("Checking if server is registered: " + serverName);
			// Attempt to get server from the server name.
			final Optional<RegisteredServer> server = Main.plugin.proxy.getServer(serverName);
			
			/**
			 * If the server was found, then attempt to connect the player
			 */
			server.ifPresent(target -> {
				Logger.debug("Attempting to connect " + player.getUsername() + " to server: " + serverName);
				
				// Attempt to connect the player
				player.createConnectionRequest(target).connect()
				.orTimeout(10, TimeUnit.SECONDS) // set timeout to 10 seconds
				.whenComplete((result, exception) -> { // Handle success/failure
					if (result.isSuccessful())
					{
						Logger.debug("Connected " + player.getUsername() + " to server: " + serverName);
						
		    			PluginMessageType.CONNECT.send(player, serverName, "true");
		    			player.getCurrentServer().ifPresent(currentServer -> PluginMessageType.CONNECT.send(currentServer, serverName, "true"));
		    			
					} else
					{
						Logger.warn("Failed to connect " + player.getUsername() + " to server: " + serverName);
						Logger.warn(exception.getCause().getMessage());
						exception.printStackTrace();
		    			
		    			player.getCurrentServer().ifPresent(currentServer -> PluginMessageType.CONNECT.send(currentServer, serverName, "false"));
		    			
					}});
				
			});
			
			if (server.isEmpty())
			{
				Logger.warn("Could not find server: " + serverName);
			}
			
		} else
		{
			Logger.warn("Could not connect to server: " + serverName);
			
			player.getCurrentServer().ifPresent(currentServer -> PluginMessageType.CONNECT.send(currentServer, serverName, "false"));
			
		}
		
	}
	
}
