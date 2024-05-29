package net.pgfmc.proxycore.commands;


import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.roles.RoleManager;

public class UnlinkCommand implements RawCommand {

	@Override
	public void execute(Invocation invocation) {
		if (!(invocation.source() instanceof Player))
		{
			invocation.source().sendMessage(Component.text("Only players can execute this command.").color(NamedTextColor.RED));
			return;
		}
		
		final Player player = (Player) invocation.source();
		final String discordUserId = RoleManager.getDiscordUserIdFromPlayerUuid(player.getUniqueId());
		
		if (discordUserId == null)
		{
			player.sendMessage(Component.text("You don't have a Discord account to unlink.").color(NamedTextColor.RED));
			return;
		}
		
		player.sendMessage(Component.text("Your Discord account has been unlinked.").color(NamedTextColor.GREEN));
		
		RoleManager.unlinkPlayerDiscord(player.getUniqueId());
		
	}
	
	@Override
	public boolean hasPermission(final Invocation invocation)
	{
		return invocation.source().hasPermission("net.pgfmc.core.unlink");
	}
	
}
