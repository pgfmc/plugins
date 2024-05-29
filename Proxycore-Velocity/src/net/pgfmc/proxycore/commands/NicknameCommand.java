package net.pgfmc.proxycore.commands;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;

public class Nickname implements SimpleCommand {
	
	@Override
	public void execute(final Invocation invocation)
	{
		if (!(invocation.source() instanceof Player))
		{
			// TODO send message
			return;
		}
		
		final Player player = (Player) invocation.source();
		final List<String> args = List.of(invocation.arguments());
		
		if (args.isEmpty())
		{
			args.add("off");
		}
		
		final String nickname = String.join("", args)
				.strip()
				.replaceAll("[^A-Za-z0-9&]", "")
				.replaceAll("[&].", "");
		
		// Grants advancement
		//
		// Will trigger with nickname reset also
		//PGFAdvancement.IMPERSONATOR.grantToPlayer(p);
		
		/*
		 * If the raw nickname is "off" or "reset" or the player's name
		 * then it will reset the nickname to Player.getName()
		 */
		if (nickname.equals("off") || nickname.equals(player.getUsername()))
		{
			pd.setData("nick", null).queue();
			pd.sendMessage(ChatColor.GOLD + "Nickname changed to " + pd.getRankedName() + ChatColor.GOLD + "!");
			
			CoreMain.updatePlayerNameplate(pd);
			
			return;
		}
		
		pd.setData("nick", nickWithColor).queue();
		pd.sendMessage(ChatColor.GOLD + "Nickname changed to " + pd.getRankedName() + ChatColor.GOLD + "!");
		
		CoreMain.updatePlayerNameplate(pd);
		
		return true;
	}
	
	@Override
	public boolean hasPermission(final Invocation invocation)
	{
		return invocation.source().hasPermission("net.pgfmc.core.nick");
	}
	
	@Override
	public List<String> suggest(final Invocation invocation)
	{
		if (!(invocation.source() instanceof Player)) return List.of();
		
		final Player player = (Player) invocation.source();
		
		return List.of("off", player.getUsername());
	}

}
