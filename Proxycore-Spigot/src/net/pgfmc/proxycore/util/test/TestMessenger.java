package net.pgfmc.proxycore.util.test;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.pgfmc.proxycore.PluginMessage;
import net.pgfmc.proxycore.util.Logger;

public class TestMessenger extends PluginMessage {

	public TestMessenger(final Player player) {
		super(PluginMessageType.GET_SERVER);
		
		PluginMessageType.GET_SERVER.send(player, new ArrayList<String>())
			.whenComplete((args, exception) -> {
				Logger.debug("------------------------------");
				
				if (exception != null)
				{
					Logger.debug("TestMessenger GET_SERVER response ERROR from Future:");
					exception.printStackTrace();
				} else
				{
					if (player.isOnline())
					{
						player.sendMessage(args.toString());
					}
					
					Logger.debug("TestMessenger GET_SERVER response from Future: " + args.toString());
				}
				
			});
		
		
		
	}

	@Override
	public void onPluginMessageReceived(Player player, List<String> args) {
		Logger.debug("------------------------------");
		
		if (player.isOnline())
		{
			player.sendMessage(args.toString());
		}
		
		Logger.debug("TestMessenger GET_SERVER response from PM: " + args.toString());
		
	}

}
