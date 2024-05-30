package net.pgfmc.core.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;
import com.moandjiezana.toml.Toml;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessage;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class PlayerDataResponse extends PluginMessage {

	public PlayerDataResponse() {
		super(PluginMessageType.PLAYER_DATA);
		
	}

	@Override
	public void onPluginMessageTypeReceived(final Player sender, ByteArrayDataInput in, final byte[] message) {
		in.readUTF();
		final String uuid = in.readUTF();
		final String data = in.readUTF();
		final OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		
		if (player == null) return;
		
		final PlayerData playerdata = PlayerData.from(player);
		final Toml toml = new Toml().read(data);
		
		toml.toMap().forEach((key, value) -> {
			playerdata.setData(key, value).queue();
			
		});
		
	}

}
