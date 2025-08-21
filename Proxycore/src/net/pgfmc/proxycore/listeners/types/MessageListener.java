package net.pgfmc.proxycore.listeners.types;


import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.messages.ChannelMessageSink;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class MessageListener extends PluginMessage {

	public MessageListener() {
		super(PluginMessageType.MESSAGE);
		
	}

	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final String chatMessage = in.readUTF();
		
		final Component chatMessageComponent = LegacyComponentSerializer.builder().build().deserialize(chatMessage);
		
		MessageHandler.sendToMinecraft(chatMessageComponent);
		
		PluginMessageType.MESSAGE.send((ChannelMessageSink) source);
		
	}

}
