package net.pgfmc.proxycore.listeners.types;

import java.util.List;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class DiscordMessage extends PluginMessage {

	public DiscordMessage(PluginMessageType type) {
		super(type);
		
	}

	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, List<String> args) {
		final String message = args.get(1);
		
		Discord.sendServerMessage(message);
		
	}

}
