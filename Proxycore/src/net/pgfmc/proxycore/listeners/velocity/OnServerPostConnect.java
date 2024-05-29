package net.pgfmc.proxycore.listeners.velocity;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.pgfmc.proxycore.util.GlobalPlayerData;
import net.pgfmc.proxycore.util.Mixins;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class OnServerPostConnect {
	
	@Subscribe
	public void onServerPostConnect(ServerPostConnectEvent e)
	{
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		final Path path = Path.of(GlobalPlayerData.dataPath + uuid + ".toml");
		final File file = Mixins.getFile(path);
		final Toml toml = new Toml().read(file);
		
		if (toml.isEmpty()) return;
		
		final Map<String, Object> data = toml.toMap();
		
		data.forEach((key, value) -> {
			PluginMessageType.PLAYER_DATA.send(player, uuid.toString(), key, (String) value); // TODO prefer not to cast like this
		});
		
		
		
		
		
	}

}
