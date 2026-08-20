package net.pgfmc.proxycore.listeners.types;

import java.util.Optional;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.controllers.Gifts;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

/**
 * Send this gift to the destination server
 */
public class GiftListener extends PluginMessage {
	public GiftListener() {
		super(PluginMessageType.GIFT);
	}
	
	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final String giftYml = in.readUTF();
		String destinationServer = "survival"; // Defaults to survival server, optional argument
		
		try {
			destinationServer = in.readUTF();
		} catch (Exception e) {}
		
		// Destination server must be valid
		if (Main.plugin.proxy.matchServer(destinationServer).isEmpty()) {
			
			Main.plugin.logger.warn("Received gift with invalid destination server: " + destinationServer);
			Main.plugin.logger.warn("Discarded gift.");
			return;
		}
		
		// Attempt to send gift to destination server
		
		Optional<RegisteredServer> maybeServer = Main.plugin.proxy.getServer(destinationServer);
		
		if (maybeServer.isPresent() && PluginMessageType.GIFT.send(maybeServer.get(), giftYml)) return;
		
		// No connection to the destination server
		Gifts.addLatentGift(destinationServer, giftYml); // Send gift later (when a connection is established)
		
	}
}
