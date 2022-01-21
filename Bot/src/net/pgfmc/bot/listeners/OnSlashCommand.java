package net.pgfmc.bot.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class OnSlashCommand implements EventListener {
    
    @Override
    public void onEvent(GenericEvent event) {
        if(!(event instanceof SlashCommandEvent)) return;
        SlashCommandEvent e = (SlashCommandEvent) event;
        if (!e.getName().equals("list")) return;
        
        List<Player> pl = new ArrayList<>(Bukkit.getOnlinePlayers());
        e.reply("Online players: " + pl).queue();
    }
}
