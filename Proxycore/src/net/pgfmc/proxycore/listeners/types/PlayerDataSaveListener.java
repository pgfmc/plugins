package net.pgfmc.proxycore.listeners.types;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataInput;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class PlayerDataSaveListener extends PluginMessage {

	public PlayerDataSaveListener() {
		super(PluginMessageType.PLAYER_DATA_SEND);
		
	}

	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, ByteArrayDataInput in, byte[] message)
	{
		in.readUTF();
		final String uuid = in.readUTF();
		final String data = in.readUTF();
		final Toml toml = new Toml().read(data);
		
		if (toml.contains("nickname") || toml.contains("discord") || toml.contains("role"))
		{
			new CompletableFuture<Void>()
			.completeOnTimeout(null, 200L, TimeUnit.MILLISECONDS)
			.whenComplete((result, exception) -> {
				Main.plugin.updateTablist();
			});
			
		}
		
		GlobalPlayerData.setData(UUID.fromString(uuid), toml);
		
	}

}
