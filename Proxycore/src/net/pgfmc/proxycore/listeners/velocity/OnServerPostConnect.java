package net.pgfmc.proxycore.listeners.velocity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public class OnServerPostConnect {
	
	@Subscribe
	public void onServerPostConnect(ServerPostConnectEvent e)
	{
		new CompletableFuture<Void>()
		.completeOnTimeout(null, 500L, TimeUnit.MILLISECONDS)
		.whenComplete((nullptr, exception) -> {
			Main.plugin.updateTablist();
		});
		
		final Player player = e.getPlayer();
		final UUID uuid = player.getUniqueId();
		
		GlobalPlayerData.propogateGlobalPlayerData(uuid);
		
	}

}
