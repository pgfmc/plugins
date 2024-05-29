package net.pgfmc.core.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.core.PGFRole;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.proxy.PluginMessage;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class PlayerDataResponse extends PluginMessage {

	public PlayerDataResponse() {
		super(PluginMessageType.PLAYER_DATA);
		
	}

	@Override
	public void onPluginMessageTypeReceived(Player sender, List<String> response) {
		final String uuid = response.get(1);
		final String key = response.get(2);
		final String value = response.get(3);
		final OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
		
		if (player == null) return;
		if (key == null) return;
		
		if (key.equals("role"))
		{
			final PGFRole role = PGFRole.get(value);
			
			if (role == null) return;
			
			PlayerData.setData(player, "role", role).queue();
			
			return;			
		}
		
		PlayerData.setData(player, key, value).queue();
		
	}

}
