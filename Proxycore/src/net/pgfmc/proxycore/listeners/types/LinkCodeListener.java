package net.pgfmc.proxycore.listeners.types;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;

import net.pgfmc.proxycore.bot.discord.slashcommands.LinkSlashCommand;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class LinkCodeListener extends PluginMessage {

	public LinkCodeListener() {
		super(PluginMessageType.LINK_CODE);
		
	}

	@Override
	public void onPluginMessageReceived(ChannelMessageSource source, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final String code = in.readUTF();
		//final String code = Encryption.decrypt(secret);
		final ServerConnection connection = (ServerConnection) source;
		final Player player = connection.getPlayer();
		final UUID uuid = player.getUniqueId();
		
		LinkSlashCommand.addLinkCode(uuid, code);
		
	}

}
