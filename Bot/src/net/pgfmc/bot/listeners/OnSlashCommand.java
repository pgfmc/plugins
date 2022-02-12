package net.pgfmc.bot.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class OnSlashCommand implements EventListener {
    
    @Override
    public void onEvent(GenericEvent event) {
        if(!(event instanceof SlashCommandEvent)) return;
        
        SlashCommandEvent e = (SlashCommandEvent) event;
        
        if (!e.getName().equals("list")) return;
        if (!e.getGuild().getId().equals(Discord.getServerChannel().getGuild().getId())) return;
        
        List<String> pl = Bukkit.getOnlinePlayers()
        		.stream()
        		.map(p -> PlayerData.from(p).getDisplayName())
        		.collect(Collectors.toList());
        e.reply("Online players: " + pl).queue();
      
    }
}
