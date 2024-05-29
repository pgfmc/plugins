package net.pgfmc.proxycore.listeners.types;

import java.util.List;
import java.util.UUID;

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
	public void onPluginMessageReceived(ServerConnection connection, Player player, List<String> args) {
		final UUID uuid = UUID.fromString(args.get(1));
		final String key = args.get(2);
		final String value = GlobalPlayerData.getData(uuid, key);
		
		getType().send(connection, uuid.toString(), key, value);
		
	}

}
