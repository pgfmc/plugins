package net.pgfmc.core.listeners.types;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataInput;

import net.pgfmc.core.util.proxy.PluginMessage;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class GiftListener extends PluginMessage {

	public GiftListener() {
		super(PluginMessageType.GIFT);
	}

	@Override
	public void onPluginMessageTypeReceived(Player sender, ByteArrayDataInput in, byte[] message) {
		try {
			in.readUTF();
			final String giftYml = in.readUTF();
			
			final YamlConfiguration giftYamlConfiguration = new YamlConfiguration();
			giftYamlConfiguration.loadFromString(giftYml);
			
			// TODO save gift to giftee's playerdata
			//final String uuid = giftYamlConfiguration.getString("player_uuid");
			//final PlayerData playerdata = PlayerData.from(UUID.fromString(uuid));
			
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
	}

}
