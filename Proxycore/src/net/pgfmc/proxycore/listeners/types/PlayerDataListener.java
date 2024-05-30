package net.pgfmc.proxycore.listeners.types;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class PlayerDataListener extends PluginMessage {

	public PlayerDataListener() {
		super(PluginMessageType.PLAYER_DATA);
		
	}

	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, ByteArrayDataInput in, final byte[] message) {
		in.readUTF();
		final UUID uuid = UUID.fromString(in.readUTF());
		
		GlobalPlayerData.propogateGlobalPlayerData(uuid);
		
	}

}
