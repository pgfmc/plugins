package net.pgfmc.proxycore.listeners.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.pgfmc.proxycore.bot.Discord;
import net.pgfmc.proxycore.bot.util.MessageHandler;
import net.pgfmc.proxycore.roles.PGFRole;
import net.pgfmc.proxycore.roles.RoleManager;

public class OnDisconnect extends MessageHandler {
	
	@Subscribe
	public void onPlayerDisconnect(DisconnectEvent e)
	{
		//e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "]" + ChatColor.RESET + " " + pd.getRankedName());
		
		final Player player = e.getPlayer();
		
		Discord.sendServerMessage("<:LEAVE:905682349239463957> " + player.getUsername()).queue();
		
		final PGFRole role = RoleManager.getRoleFromPlayerUuid(player.getUniqueId());
		final Builder textComponentBuilder = Component.text();
		
		textComponentBuilder.append(Component.text("[")
				.color(NamedTextColor.GRAY));
		
		textComponentBuilder.append(Component.text("-")
				.color(NamedTextColor.RED));
		
		textComponentBuilder.append(Component.text("] ")
				.color(NamedTextColor.GRAY));
		
		textComponentBuilder.append(Component.text(((role.compareTo(PGFRole.STAFF) <= 0) ? PGFRole.STAFF_DIAMOND : "") + player.getUsername())
				.color(role.getColor()));
		
		sendToMinecraft(textComponentBuilder.build());
		
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {}
	
}
