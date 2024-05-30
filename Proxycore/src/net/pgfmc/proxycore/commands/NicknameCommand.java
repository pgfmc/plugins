package net.pgfmc.proxycore.commands;

import java.util.List;
import java.util.UUID;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.Main;
import net.pgfmc.proxycore.util.GlobalPlayerData;

public class NicknameCommand implements SimpleCommand {
	
	@Override
	public void execute(final Invocation invocation)
	{
		if (!(invocation.source() instanceof Player))
		{
			invocation.source().sendMessage(Component.text("Only players can execute this command.").color(NamedTextColor.RED));
			return;
		}
		
		final Player player = (Player) invocation.source();
		final UUID uuid = player.getUniqueId();
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
			GlobalPlayerData.setData(uuid, "nickname", player.getUsername());
			
			player.sendMessage(Component.text()
					.append(Component.text("Nickname changed to ")
							.color(NamedTextColor.GOLD))
					.append(GlobalPlayerData.getRankedName(uuid))
					.append(Component.text("!")
							.color(NamedTextColor.GOLD))
					.build());
			
			Main.plugin.updateTablist();
			
			return;
		}
		
		GlobalPlayerData.setData(uuid, "nickname", nickname);
		
		player.sendMessage(Component.text()
				.append(Component.text("Nickname changed to ")
						.color(NamedTextColor.GOLD))
				.append(GlobalPlayerData.getRankedName(uuid))
				.append(Component.text("!")
						.color(NamedTextColor.GOLD))
				.build());
		
		Main.plugin.updateTablist();
		
		return;
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
