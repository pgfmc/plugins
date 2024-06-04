package net.pgfmc.proxycore.listeners.types;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class PlayerDataListener extends PluginMessage {

	public PlayerDataListener() {
		super(PluginMessageType.PLAYER_DATA);
		
	}

	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final UUID uuid = UUID.fromString(in.readUTF());
		
		GlobalPlayerData.propogateGlobalPlayerData(uuid);
		
	}

}
