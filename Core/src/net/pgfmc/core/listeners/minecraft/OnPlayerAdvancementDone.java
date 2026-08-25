package net.pgfmc.core.listeners.minecraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.TranslatableComponentDecoder;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerAdvancementDone implements Listener {
	
	@EventHandler
	public void advancementDone(PlayerAdvancementDoneEvent e)
	{
		if (e.getAdvancement().getDisplay() == null) return;
		
		final Player player = e.getPlayer();
		final PlayerData playerdata = PlayerData.from(player);
		final Advancement advancement = e.getAdvancement();
		final Component advancementMessageComponent = e.message();
		
		if (advancementMessageComponent instanceof TranslatableComponent advancementMessageTranslatableComponent) {
			if (!advancementMessageTranslatableComponent.arguments().isEmpty()) {
				final List<Component> newArgs = new ArrayList<>();
				newArgs.add(playerdata.getRankedName());
				
				advancementMessageTranslatableComponent.arguments(newArgs);
				e.message(advancementMessageTranslatableComponent);
			}
			
			final String plaintext = TranslatableComponentDecoder.translate(advancementMessageTranslatableComponent);
			PluginMessageType.DISCORD_MESSAGE.send(player, "<:dwarf:1191762269261017119> " + playerdata.getDisplayName() + " " + plaintext);
		}
		
		

        //String advancementComponent = ((TextComponent) advancement.getDisplay().title()).content();
        //Logger.log(advancement.displayName().toString());
        //Logger.log(GlobalTranslator.translator().translate(((TranslatableComponent) advancement.displayName()), Locale.US).toString());



		/*
        TextComponent translated = (TextComponent) GlobalTranslator.render(advancement.getDisplay().title(), Translation.DISCORD_LOCALE);


        Logger.log(translated.toString());


		String advancementMessage = "";
		
		switch (advancement.getDisplay().frame())
		{
			case GOAL:
				advancementMessage = "has reached the goal [" + advancement.getDisplay().title() + "]!";
				break;
			case CHALLENGE:
				advancementMessage = "has completed the challenge [" + advancement.getDisplay().title() + "]!";
				break;
			case TASK:
			default:
				advancementMessage = "has made the advancement [" + advancement.getDisplay().title() + "]!";
				break;
		}
		*/
		
		/*
		 * No easy way to cancel/set the advancement message
		 * 
		final String finalAdvancementMessage = advancementMessage;
		
		PluginMessageType.MESSAGE.send(player, playerdata.getRankedName() + " " + advancementMessage)
		.orTimeout(1000L, TimeUnit.MILLISECONDS) // Should only time out if the proxy isn't online
		.whenComplete((result, exception) -> {
			if (exception != null)
			{
				CoreMain.plugin.getServer().broadcastMessage(playerdata.getRankedName() + " " + finalAdvancementMessage); // requires final or effectively final
			}
		});
		*/
		
		//PluginMessageType.DISCORD_MESSAGE.send(player, "<:dwarf:1191762269261017119> " + playerdata.getDisplayName() + " " + translated);
		//PluginMessageType.DISCORD_MESSAGE.send(player, "<:dwarf:1191762269261017119> " + playerdata.getDisplayName() + advancementMessage);
	}
}
