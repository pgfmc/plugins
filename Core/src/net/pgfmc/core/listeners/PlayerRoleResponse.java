package net.pgfmc.core.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.pgfmc.core.PGFRole;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.proxy.PluginMessage;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class PlayerRoleResponse extends PluginMessage {

	public PlayerRoleResponse() {
		super(PluginMessageType.PLAYER_ROLE);
		
	}

	@Override
	public void onPluginMessageTypeReceived(Player sender, List<String> response) {
		final String playerUuidResponse = response.get(1);
		final String discordUserId = response.get(2);
		final String roleName = response.get(3);
		final PlayerData playerdata = PlayerData.from(UUID.fromString(playerUuidResponse));
		final PGFRole role = PGFRole.get(roleName);
		
		if (playerdata == null)
		{
			Logger.warn("PluginMessageType PLAYER_ROLE: playerdata is null");
			return;
		}
		
		if (role == null)
		{
			Logger.warn("PluginMessageType PLAYER_ROLE: role is null");
			return;
		}
		
		playerdata.setData("role", role);
		
		if (discordUserId == null || discordUserId.isBlank()) return;
		
		playerdata.setData("Discord", discordUserId);
		
	}

}
