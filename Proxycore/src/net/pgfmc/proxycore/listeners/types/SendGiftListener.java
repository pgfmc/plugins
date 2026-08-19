package net.pgfmc.proxycore.listeners.types;

import java.util.Optional;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class SendGiftListener extends PluginMessage {
	public SendGiftListener() {
		super(PluginMessageType.GIFT);
	}
	
	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final String giftYml = in.readUTF();
		String destinationServer = "survival"; // Defaults to survival server
		
		try {
			destinationServer = in.readUTF(); // Optional argument
		} catch (Exception e) {}
		
		Optional<RegisteredServer> maybeServer = Main.plugin.proxy.getServer(destinationServer);
		
		if (maybeServer.isPresent() && PluginMessageType.GIFT.send(maybeServer.get(), giftYml)) return;
		
		// TODO save gift to some queue and wait for any player to join on the destination server before sending gift
		
	}
}
