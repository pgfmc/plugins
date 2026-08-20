package net.pgfmc.proxycore.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.Mixins;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

/**
 * Manages gifts that could not be sent to the destination server
 * at the time the Gift plugin message was sent.
 * 
 * Keeps latent gifts in memory until a valid connection to a
 * destination server is established (by any player)
 * 
 * Saves/loads gifts to/from file via Java serialization
 */
public class Gifts {
	
	final private static Path latentGiftsFilePath = Path.of(Main.plugin.dataDirectory + "gifts-latent.ser");
	
	// Maps destination server to list of latent gifts (gifts are YAML as string)
	private static Map<String, List<String>> latentGifts = new HashMap<>();
	
	/**
	 * Save a gift when it can't be sent to a destination server.
	 * The gift will be sent when possible.
	 * 
	 * @param destinationServer
	 * @param giftYml
	 */
	public static void addLatentGift(String destinationServer, String giftYml) {
		// Add this gift to the appropriate list
		latentGifts.computeIfAbsent(destinationServer.toLowerCase(), key -> new ArrayList<>()).add(giftYml);
	}
	
	@Subscribe
	public void onInitialize(ProxyInitializeEvent event) {
		try {
			// Get latent gifts file
			
			final File latentGiftsFile = Mixins.getFile(latentGiftsFilePath);
			final FileInputStream fin = new FileInputStream(latentGiftsFile);
			final ObjectInputStream in = new ObjectInputStream(fin);
			
			// Set latent gifts
			
			@SuppressWarnings("unchecked")
			Map<String, List<String>> loadedLatentGifts = (Map<String, List<String>>) in.readObject();
			latentGifts = Optional.ofNullable(loadedLatentGifts).orElse(new HashMap<>());
			
			in.close();
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Subscribe
	public void onShutdown(ProxyShutdownEvent event) {
		if (latentGifts.isEmpty()) return;
		
		try {
			// Create and write latent gifts map to file
			
			final File latentGiftsFile = Mixins.getFile(latentGiftsFilePath);
			FileOutputStream fout = new FileOutputStream(latentGiftsFile);
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(latentGifts);
			
			out.close();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Subscribe
	public void onPlayerConnect(ServerPostConnectEvent event) {
		// Check that the connection is still established
		final Optional<ServerConnection> maybeServerConnection = event.getPlayer().getCurrentServer();
		if (maybeServerConnection.isEmpty()) return;
		
		final ServerConnection serverConnection = maybeServerConnection.get();
		
		// Get latent gifts for this server
		final String serverName = serverConnection.getServer().getServerInfo().getName().toLowerCase();
		final List<String> latentGiftsThisServer = latentGifts.get(serverName);
		
		if (latentGiftsThisServer == null || latentGiftsThisServer.isEmpty()) return;
		
		// Attempt to send latent gifts
		latentGiftsThisServer.removeIf(gift -> PluginMessageType.GIFT.send(serverConnection, gift));
		
	}

}
