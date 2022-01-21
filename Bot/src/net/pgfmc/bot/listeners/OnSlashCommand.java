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
    public void onEvent(GenericEvent e) {
        if(!(e instanceof SlashCommandEvent)) return;
        List<Player> pl = new ArrayList<>(Bukkit.getOnlinePlayers());
        ((SlashCommandEvent) e).reply("Here are the list of online players" + pl).queue();
    }
}