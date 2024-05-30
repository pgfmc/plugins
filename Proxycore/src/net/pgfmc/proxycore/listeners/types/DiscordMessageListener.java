package net.pgfmc.proxycore.listeners.types;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class DiscordMessageListener extends PluginMessage {

	public DiscordMessageListener() {
		super(PluginMessageType.DISCORD_MESSAGE);
		
	}

	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, ByteArrayDataInput in, final byte[] message) {
		in.readUTF();
		final String discordMessage = in.readUTF();
		
		Discord.sendServerMessage(discordMessage);
		
	}

}
