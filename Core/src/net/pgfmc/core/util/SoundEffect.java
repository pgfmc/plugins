package net.pgfmc.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.pgfmc.core.api.playerdata.PlayerData;

public enum SoundEffect {
    ERROR(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F),
    DING(Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F),
    PING(Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 2F),
    WORKING(Sound.BLOCK_ANVIL_USE, 1.0F, 2.0F),
    DENY(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F),
    SUCCESS(Sound.ENTITY_PLAYER_LEVELUP, 0.2F, 1.0F),
    TELEPORT(Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F),
    CLICK(Sound.BLOCK_LEVER_CLICK, 1.0F, 1.5F);


    private Sound sound;
    private float volume;
    private float pitch;

    SoundEffect(Sound sound, float volume, float pitch)
    {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void play(Player player)
    {
        if (!player.isOnline()) return;

        player.playSound(player.getLocation(), this.sound, this.volume, this.pitch);
    }

    public void play(PlayerData player)
    {
        if (!player.isOnline()) return;
        this.play(player.getPlayer());
    }

    public void play(Location location)
    {
        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(location, this.sound, this.volume, this.pitch));
    }

}