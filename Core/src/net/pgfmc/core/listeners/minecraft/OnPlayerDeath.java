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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.pgfmc.core.CoreMain;
import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.TranslatableComponentDecoder;
import net.pgfmc.core.util.proxy.PluginMessageType;

public class OnPlayerDeath implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		final Player player = e.getEntity();
		final PlayerData playerdata = PlayerData.from(player);
		final Component deathMessage = e.deathMessage();
		
		if (deathMessage instanceof TranslatableComponent deathMessageTransComponent) {
			final List<TranslationArgument> args = deathMessageTransComponent.arguments();
			final List<Component> newArgs = new ArrayList<>();
			
			final Entity causingEntity = e.getDamageSource().getCausingEntity();
	        PlayerData causingPlayer = null;

	        if (causingEntity instanceof Player) {
	            final Player causingEntityPlayer = (Player) causingEntity;
	            causingPlayer = PlayerData.from(causingEntityPlayer);
	        }
			
			for (int i = 0; i < args.size(); i++) {
	            final String deadPlayer = PlainTextComponentSerializer.plainText().serialize(args.get(i).asComponent());
	            
	            if (deadPlayer.equals(player.getName())) {
	            	newArgs.add(i, playerdata.getRankedName());
                } else if (causingPlayer != null && deadPlayer.equals(causingPlayer.getDisplayName())) {
                    newArgs.add(i, causingPlayer.getRankedName());
                } else {
                    newArgs.add(Component.text(deadPlayer));
                }
	        }
			
			deathMessageTransComponent.arguments(newArgs);
			e.deathMessage(deathMessageTransComponent);
			
			final String translated = TranslatableComponentDecoder.translate(deathMessageTransComponent);
			PluginMessageType.DISCORD_MESSAGE.send(playerdata.getPlayer(), "<:DEATH:907865162558636072> " + translated);
			PluginMessageType.MESSAGE.send(player, deathMessageTransComponent);
		}




		//TranslatableComponent deathMessage = (TranslatableComponent) e.deathMessage();

        //Logger.warn(deathMessage.arguments().toString());

        //List<TranslationArgument> args = deathMessage.arguments();
        //List<String> newArgs = new ArrayList<>();


        //final Entity causingEntity = e.getDamageSource().getCausingEntity();
        //PlayerData causingPlayer = null;

        //if (causingEntity instanceof Player) {
        //    final Player causingEntityPlayer = (Player) causingEntity;
        //    causingPlayer = PlayerData.from(causingEntityPlayer);
        //}



        //MessageFormat message = GlobalTranslator.translator().translate(deathMessage.key(), Translation.DISCORD_LOCALE);

        //for (int i = 0; i < args.size(); i++) {
        //    Component arg1 = args.get(i).asComponent();
        //    if (arg1 != null && arg1 instanceof TextComponent) {
        //        String deadPlayer = ((TextComponent) arg1).content();

        //        if (deadPlayer.equals(player.getName())) {
        //            newArgs.add(i, playerdata.getDisplayName());
        //        } else if (causingPlayer != null && deadPlayer.equals(causingPlayer.getName())) {
        //            newArgs.add(i, causingPlayer.getDisplayName());
        //        } else {
        //            newArgs.add(deadPlayer);
        //        }
        //    }
        //}


        //String output = message.format(newArgs.toArray(), new StringBuffer(), new FieldPosition(0)).toString();


        //TextComponent translated = ((TextComponent) GlobalTranslator.render(deathMessage, Translation.DISCORD_LOCALE));
        
        //Logger.log(GlobalTranslator.translator().translate(deathMessage, Translation.DISCORD_LOCALE).toString());
        //Logger.log(output);


        //String message = playerdata.getDisplayName() + " has died. (" + CoreMain.plugin.serverDisplayName() + ")";




//		PluginMessageType.MESSAGE.send(player, deathMessage);
		//PluginMessageType.DISCORD_MESSAGE.send(playerdata.getPlayer(), "<:DEATH:907865162558636072> " + message);
		e.deathMessage(null);
	}
}
