package net.pgfmc.proxycore.util.pluginmessage.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.commands.PlayerCommand;
import net.pgfmc.proxycore.util.pluginmessage.PluginMessageType;

/**
 * An admin command for testing plugin messages.
 * 
 * Send a plugin message. Response will be sent back to the player.
 */
public class TestPluginMessageCommand extends PlayerCommand {

	public TestPluginMessageCommand(String name) {
		super(name);
		
	}

	@Override
	public List<String> tabComplete(PlayerData pd, String alias, String[] args) {
		if (args.length > 1) return new ArrayList<String>();
		
		// All plugin message types
		return Stream.of(PluginMessageType.values()).map(type -> type.getSubchannel()).collect(Collectors.toList());
	}

	@Override
	public boolean execute(PlayerData pd, String alias, String[] args) {
		if (args.length == 0)
		{
			pd.sendMessage(ChatColor.RED + "Incorrect command usage: please provide an argument.");
			return true;
		}
		
		final PluginMessageType type = PluginMessageType.from(args[0]);
		
		if (type == null)
		{
			pd.sendMessage(ChatColor.RED + "Invalid plugin message type: " + args[0]);
			return true;
		}
		
								// new array list is important here, because removing an element also removes the
		final List<String> argsToSend = new ArrayList<>(Arrays.asList(args)); // element from the String[] args
		argsToSend.remove(0);
		
		type.send(pd.getPlayer(), argsToSend)
			.whenComplete((messageArgs, exception) -> {
				if (exception != null)
				{
					exception.printStackTrace();
					return;
				}
				
				if (!pd.isOnline()) return;
				
				pd.sendMessage(ChatColor.LIGHT_PURPLE + messageArgs.toString());
				
			});
		
		return true;
	}

}
