package net.pgfmc.proxycore.listeners.types;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.io.ByteArrayDataInput;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;
import net.pgfmc.proxycore.util.roles.PGFRole;
import net.pgfmc.proxycore.util.roles.RoleManager;

public class PlayerDataSaveListener extends PluginMessage {

	public PlayerDataSaveListener() {
		super(PluginMessageType.PLAYER_DATA_SEND);
		
	}

	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message)
	{
		in.readUTF();
		final String uuid = in.readUTF();
		final String data = in.readUTF();
		final UUID playerUuid = UUID.fromString(uuid);
		final Toml toml = new Toml().read(data);

        GlobalPlayerData playerData = GlobalPlayerData.fromUuid(playerUuid);

        String nickname = toml.getString("nickname");
        String discord = toml.getString("discord");
        PGFRole role = PGFRole.get(toml.getString("role"));
        String username = toml.getString("username");

        if (nickname != null) { playerData.nickname = nickname;}
        if (discord != null) { playerData.discord = discord;}
        if (role != null) { playerData.role = role;}
        if (username != null) { playerData.username = username;}

        playerData.save();
		RoleManager.updatePlayerRole(playerUuid);
		
		if (toml.contains("nickname") || toml.contains("discord") || toml.contains("role"))
		{
			new CompletableFuture<Void>()
			.completeOnTimeout(null, 400L, TimeUnit.MILLISECONDS)
			.whenComplete((result, exception) -> {
				Main.plugin.updateTablist();
			});
			
		}
		
	}

}
