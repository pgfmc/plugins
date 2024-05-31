package net.pgfmc.proxycore.listeners.types;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Logger;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class PingServerListener extends PluginMessage {

	public PingServerListener() {
		super(PluginMessageType.PING_SERVER);
		
	}
	
	/**
	 * The PingServer subchannel is used for checking if a server is online and reachable by the proxy.
	 * 
	 * The response says if it could ping the server.
	 * 
	 * PLUGIN MESSAGE FORM: PingServer, <server name>
	 * RESPONSE PLUGIN MESSAGE FORM: PingServer, <server name>, <true/false>
	 */
	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, ByteArrayDataInput in, final byte[] message) {
		in.readUTF();
		final String serverName = in.readUTF();
		final Optional<RegisteredServer> server = Main.plugin.proxy.getServer(serverName);
		
		Logger.debug("Checking if server is registered: " + serverName);
		
		server.ifPresent(target -> {
			final InetSocketAddress address = target.getServerInfo().getAddress();
			final Socket socket = new Socket();
			
			Logger.debug("Address: " + address);
			Logger.debug("Socket: " + socket);
			
			/**
			 * The Completable Future allows for a function to be ran once it is complete in the future.
			 * 
			 * The thread below will complete the future when it is finished trying to establish a connection.
			 */
			final CompletableFuture<Boolean> future = new CompletableFuture<Boolean>()
					.completeOnTimeout(Boolean.FALSE, 5L, TimeUnit.SECONDS);
			
			/**
			 * Send a PluginMessage of the results when complete.
			 */
			future.whenComplete((isOnline, exception) -> {
				Logger.debug("CompletableFuture for PingServer is complete.");
				
				if (exception != null && !(exception instanceof ConnectException))
				{
					exception.printStackTrace();
				} else
				{
	    			PluginMessageType.PING_SERVER.send(connection, serverName, isOnline);
	    			
				}
				
			});
			
			/**
			 * This thread will complete the Future when finished.
			 */
			new Thread(new Runnable() {

				@Override
				public void run() {
					Boolean isOnline = Boolean.FALSE;
					
					try {
						Logger.debug("Attempting to ping server: " + serverName);
	    				
	    				// Try to connect to the server
						socket.connect(address, 3000); // 3 second timeout
						
						Logger.debug("Successfully pinged server (online): " + serverName);
						
						// No errors means successfully connected
						isOnline = Boolean.TRUE;
						
	    			} catch (SocketTimeoutException e) {
	    				Logger.debug("Could not ping server (offline): " + serverName);
					} catch (IOException e) {
						Logger.warn("Error while trying to ping server: " + serverName);
						
						e.printStackTrace();
					} finally {
						// Complete the Future and return online status
						future.complete(isOnline);
						
						try {
							// Must close socket if connected
							if (socket.isConnected())
							{
								socket.close();
								Logger.debug("Closed the connection.");
								
							}
							
						} catch (IOException e) {
							Logger.warn("Error while trying to close the socket for server: " + serverName);
							
							e.printStackTrace();
						}
					}
					
				}
    			
    		}).start();
			
		});
		
		if (server.isEmpty())
		{
			Logger.warn("Could not find server: " + serverName);
		}
		
		
	}

}
