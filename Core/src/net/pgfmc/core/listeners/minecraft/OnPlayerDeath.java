package net.pgfmc.core.listeners.minecraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.translation.GlobalTranslator;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Logger;
import net.pgfmc.core.util.Translation;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		final Player player = e.getEntity();
		final PlayerData playerdata = PlayerData.from(player);
		TranslatableComponent deathMessage = (TranslatableComponent) e.deathMessage();

        Logger.warn(deathMessage.arguments().toString());

        List<TranslationArgument> args = deathMessage.arguments();
        List<TranslationArgument> newArgs = new ArrayList<>();


        final Entity causingEntity = e.getDamageSource().getCausingEntity();
        PlayerData causingPlayer = null;

        if (causingEntity instanceof Player) {
            final Player causingEntityPlayer = (Player) causingEntity;
            causingPlayer = PlayerData.from(causingEntityPlayer);
        }



        for (int i = 0; i < args.size(); i++) {
            Component arg1 = args.get(i).asComponent();
            if (arg1 != null && arg1 instanceof TextComponent) {
                String deadPlayer = ((TextComponent) arg1).content();

                if (deadPlayer.equals(player.getName())) {
                    newArgs.add(i, TranslationArgument.component(playerdata.getRankedName()));
                } else if (causingPlayer != null && deadPlayer.equals(causingPlayer.getName())) {
                    newArgs.add(i, TranslationArgument.component(causingPlayer.getRankedName()));
                } else {
                    newArgs.add(args.get(i));
                }
            }
        }

        deathMessage.arguments(newArgs); 

        TextComponent translated = ((TextComponent) GlobalTranslator.render(deathMessage, Translation.DISCORD_LOCALE));
        
        //Logger.log(GlobalTranslator.translator().translate(deathMessage, Translation.DISCORD_LOCALE).toString());
        Logger.log(translated.toString());






//		PluginMessageType.MESSAGE.send(player, deathMessage);
		PluginMessageType.DISCORD_MESSAGE.send(playerdata.getPlayer(), "<:DEATH:907865162558636072> " + translated.content());
		
		//e.deathMessage(null);
	}
}
