package net.pgfmc.proxycore.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.roles.RoleManager;
import net.pgfmc.proxycore.util.Logger;

public final class LinkCommand implements RawCommand {
	
	private final static Map<String, Player> CODE_PLAYER_PAIR = new HashMap<>();

	@Override
	public void execute(final Invocation invocation) {
		
		if (!(invocation.source() instanceof Player))
		{
			invocation.source().sendMessage(Component.text("Only players can execute this command.").color(NamedTextColor.RED));
			return;
		}
		
		final Player player = (Player) invocation.source();
		final String discordUserId = RoleManager.getDiscordUserIdFromPlayerUuid(player.getUniqueId());
		
		if (discordUserId != null && !discordUserId.isBlank())
		{
			player.sendMessage(Component.text()
					.append(Component.text("Your Discord has already been linked.")
							.color(NamedTextColor.RED))
					.appendNewline()
					.append(Component.text("Use ")
							.color(NamedTextColor.RED))
					.append(Component.text("/unlink")
							.color(NamedTextColor.WHITE))
					.append(Component.text(" to unlink your account.")
							.color(NamedTextColor.RED))
					.build());
			
			return;
		}
		
		player.sendMessage(Component.text("Generating code...").color(NamedTextColor.GREEN));
		
		final String code = generateCode();
		CODE_PLAYER_PAIR.put(code, player);
		
		player.sendMessage(Component.text()
				.append(Component.text("Do ")
						.color(NamedTextColor.GOLD))
				.append(Component.text("/link " + code)
						.color(NamedTextColor.WHITE))
				.append(Component.text(" in any channel in the ")
						.color(NamedTextColor.GOLD))
				.append(Component.text(Discord.getAssociatedGuild().getName())
						.decorate(TextDecoration.BOLD))
				.appendNewline()
				.append(Component.text(" Guild to finish linking your account: ")
						.color(NamedTextColor.GOLD))
				.append(Component.text("https://discord.gg/zdxeREe")
						.color(NamedTextColor.BLUE))
				.build());
		
		return;
	}
	
	@Override
	public boolean hasPermission(final Invocation invocation)
	{
		return invocation.source().hasPermission("net.pgfmc.core.link");
	}
	
	public static Player checkPlayerLinkCode(final String code)
	{
		// Check if map contains the code
		if (!CODE_PLAYER_PAIR.containsKey(code)) return null;
		
		final Player player = CODE_PLAYER_PAIR.get(code);
		
		// Remove player from map if codes match
		CODE_PLAYER_PAIR.remove(code);
		
		return player;
	}
	
	/**
	 * Get a randomly generated 4 digit code that isn't taken
	 * @return The 4 digit unique code
	 */
	private final String generateCode()
	{
		final Random random = new Random(System.currentTimeMillis());
		// range is 1000 to 9999
		String code = String.valueOf(random.nextInt(9999 - 1000) + 1000);
		
		Logger.debug("Generating code: Code is " + code);
		
		while (CODE_PLAYER_PAIR.containsKey(code))
		{
			Logger.debug("Generating code: " + code + " taken, generating new code");
			code = String.valueOf(random.nextInt(9999 - 1000) + 1000);
			
		}
		
		Logger.debug("Generating code: Code is " + code);
		
		return code;
	}
	
}