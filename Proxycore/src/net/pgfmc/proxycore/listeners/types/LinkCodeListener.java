package net.pgfmc.proxycore.listeners.types;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;

import net.pgfmc.proxycore.bot.discord.slashcommands.LinkSlashCommand;
import net.pgfmc.proxycore.util.proxy.PluginMessage;
import net.pgfmc.proxycore.util.proxy.PluginMessageType;

public class LinkCodeListener extends PluginMessage {

	public LinkCodeListener() {
		super(PluginMessageType.LINK_CODE);
		
	}

	@Override
	public void onPluginMessageReceived(ServerConnection connection, Player player, ByteArrayDataInput in, byte[] message) {
		in.readUTF();
		final String code = in.readUTF();
		//final String code = Encryption.decrypt(secret);
		final UUID uuid = player.getUniqueId();
		
		LinkSlashCommand.addLinkCode(uuid, code);
		
	}

}
