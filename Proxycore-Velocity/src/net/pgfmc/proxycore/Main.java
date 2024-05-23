package net.pgfmc.proxycore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.pgfmc.proxycore.util.Logger;

@Plugin(id = "pgf", name = "Proxycore", version = "0.0.0",
        url = "https://www.pgfmc.net", description = "Core functionality for the proxy", authors = {"PGF"})
/**
 * This is a Velocity plugin (NOT a Minecraft JavaPlugin). It runs on the Velocity proxy.
 * 
 * It can send and receive Plugin Messages (packets). This can be used to communicate
 * things from one server to another like:
 * chat messages, command executions, or connecting a player to a server.
 * 
 * This plugin listens for and sends packets/plugin messages on the pgf:main Channel Identifier.
 */
public class Main implements Logger {
	
	/**
	 * The Channel Identifier for this Velocity plugin.
	 */
	public static final MinecraftChannelIdentifier INDENTIFIER = MinecraftChannelIdentifier.from("pgf:main");
	
	public static Main plugin;
    public final ProxyServer proxy;
    public final org.slf4j.Logger logger;
    public final Path dataDirectory;
    
    /**
     * Automatically injects the ProxyServer, Logger, and Path into the constructor.
     * 
     * @param proxy
     * @param logger
     * @param dataDirectory
     */
    @Inject
    public Main(ProxyServer proxy, org.slf4j.Logger logger, @DataDirectory Path dataDirectory) {
    	this.plugin = this;
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        
    }
    
    /**
     * The listener for when the proxy initializes.
     * 
     * @param event
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
    	Logger.log("=============");
    	Logger.log("  Proxycore  ");
    	Logger.log("  -- PGF --  ");
    	Logger.log("=============");
    	
    	/**
    	 * Register listeners
    	 */
    	proxy.getChannelRegistrar().register(INDENTIFIER);
    	
    }
    
    /**
     * The listener for plugin messages/packets.
     * 
     * @param event
     */
    @Subscribe
    public void onPluginMessageFromPlugin(PluginMessageEvent event)
    {
    	Logger.debug("Received a plugin message: ");
    	Logger.debug("Identifier: " + event.getIdentifier());
    	Logger.debug("Source: " + event.getSource());
    	
    	if (!(event.getSource() instanceof ServerConnection)) return;
    	if (event.getIdentifier() != INDENTIFIER) return;
    	
    	final ServerConnection sourceServer = (ServerConnection) event.getSource();
    	final Player player = sourceServer.getPlayer();
    	final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
    	// The subchannel is always first in data
    	final String subchannel = in.readUTF();
    	
    	Logger.debug("Player: " + player.getUsername());
    	Logger.debug("Subchannel: " + subchannel);
    	
    	/**
    	 * [Connect]
    	 * 
    	 * The Connect subchannel is used for sending this player to
    	 * the specified server.
    	 * 
    	 * PLUGIN MESSAGE FORM (pgf:main): Connect, <server name>
    	 * 
    	 * ---------------------
    	 * 
    	 * [ConnectReponse]
    	 * 
    	 * The ConnectResponse subchannel is used for responding to
    	 * the Connect subchannel. It says if the connection attempt was successful or not.
    	 * 
    	 * PLUGIN MESSAGE FORM (pgf:main): ConnectResponse, <server name>, <true/false>
    	 */
    	if (subchannel.equals("Connect"))
    	{
    		// The name of the requested server transfer
    		final String serverName = in.readUTF();
    		
    		if (serverName != null && !serverName.equalsIgnoreCase(sourceServer.getServerInfo().getName()))
    		{
    			Logger.debug("Checking if server is registered: " + serverName);
    			// Attempt to get server from the server name.
    			final Optional<RegisteredServer> server = proxy.getServer(serverName);
    			
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
							
							final ByteArrayDataOutput out = ByteStreams.newDataOutput();
			    			out.writeUTF("ConnectResponse");
			    			out.writeUTF(serverName);
			    			out.writeUTF("true"); // Success
			    			
			    			player.getCurrentServer().ifPresent(connection -> connection.sendPluginMessage(INDENTIFIER, out.toByteArray()));
			    			
						} else if (exception.getCause() instanceof IllegalStateException)
						{
							player.transferToHost(target.getServerInfo().getAddress());
							
						} else
						{
							
							Logger.warn("Failed to connect " + player.getUsername() + " to server: " + serverName);
							Logger.warn(exception.getCause().getMessage());
							exception.printStackTrace();
							
							final ByteArrayDataOutput out = ByteStreams.newDataOutput();
							out.writeUTF("ConnectResponse");
							out.writeUTF(serverName);
			    			out.writeUTF("false"); // Failure
			    			
			    			player.getCurrentServer().ifPresent(connection -> connection.sendPluginMessage(INDENTIFIER, out.toByteArray()));
			    			
						}});
					
				});
				
				if (server.isEmpty())
				{
					Logger.warn("Could not find server: " + serverName);
				}
				
				
				
    		} else
    		{
    			Logger.warn("Could not connect to server: " + serverName);
    			
    			final ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("ConnectResponse");
				out.writeUTF(serverName);
    			out.writeUTF("false");
    			
    			player.getCurrentServer().ifPresent(connection -> connection.sendPluginMessage(INDENTIFIER, out.toByteArray()));
    			
    		}
    		
    		return;
    	}
    	
    	/**
    	 * [PingServer] and [PingServerResponse]
    	 * 
    	 * The PingServer subchannel is used for checking if a server is online and reachable by the proxy.
    	 * 
    	 * It returns a PingServerResponse that says if it could ping the server.
    	 * 
    	 * PLUGIN MESSAGE FORM (pgf:main): PingServer, <server name>
    	 * PLUGIN MESSAGE FORM (pgf:main): PingServerResponse, <server name>, <true/false>
    	 */
    	if (subchannel.equals("PingServer"))
    	{
    		final String serverName = in.readUTF();
    		final Optional<RegisteredServer> server = proxy.getServer(serverName);
    		
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
    			final CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
    			
    			/**
    			 * Send a PluginMessage of the results when complete.
    			 */
    			future.whenComplete((isOnline, exception) -> {
    				Logger.debug("CompletableFuture for PingServer is complete.");
    				
    				if (exception != null)
    				{
    					exception.printStackTrace();
    				} else
    				{
    					final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    					out.writeUTF("PingServerResponse");
    					out.writeUTF(serverName);
    	    			out.writeUTF(isOnline.toString());
    	    			
    	    			player.getCurrentServer().ifPresent(connection -> connection.sendPluginMessage(INDENTIFIER, out.toByteArray()));
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
    	
    	if (subchannel.equals("ConnectOther"))
    	{
    		// TODO make connect other command
    		return;
    	}
    	
    }
    
}
