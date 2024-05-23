package net.pgfmc.proxycore.util.test;

import java.util.List;

import org.bukkit.entity.Player;

import net.pgfmc.proxycore.PluginMessage;
import net.pgfmc.proxycore.util.Logger;

public class TestMessengerAlsoAnother extends PluginMessage {
	
	public TestMessengerAlsoAnother(final Player player) {
		super(PluginMessageType.CONNECT);
		
		/*
		Messenger.CONNECT.send(player, Arrays.asList("past"))
		.whenComplete((args, exception) -> {
			if (exception != null)
			{
				Logger.debug("TestMessengerAlsoAnother CONNECT response ERROR from Future:");
				exception.printStackTrace();
			} else
			{
				if (player.isOnline())
				{
					player.sendMessage(args.toString());
				}
				
				Logger.debug("TestMessengerAlsoAnother CONNECT response from Future: " + args.toString());
			}
			
		});
		*/
		
	}

	@Override
	public void onPluginMessageReceived(Player player, List<String> args) {
		Logger.debug("------------------------------");
		
		if (player.isOnline())
		{
			player.sendMessage(args.toString());
		}
		
		Logger.debug("TestMessengerAlsoAnother CONNECT response from PM: " + args.toString());
		
	}
}
