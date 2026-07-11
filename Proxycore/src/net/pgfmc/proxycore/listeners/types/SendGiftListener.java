package net.pgfmc.proxycore.listeners.types;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class SendGiftListener extends PluginMessage {
	public SendGiftListener() {
		super(PluginMessageType.SEND_GIFT);
	}
	
	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final String gift = in.readUTF();
	}
}
