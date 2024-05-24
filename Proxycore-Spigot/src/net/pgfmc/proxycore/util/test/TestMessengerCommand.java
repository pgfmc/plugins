package net.pgfmc.proxycore.util.test;

import java.util.List;

import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;

public class TestMessengerCommand extends PlayerCommand {

	public TestMessengerCommand(String name) {
		super(name);
		
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		final Player player = pd.getPlayer();
		
		if (!player.isOnline()) return true;
		
		new TestMessenger(player);
		new TestMessengerAlso(player);
		//new TestMessengerAlsoAnother(player);
		
		return true;
	}

}
