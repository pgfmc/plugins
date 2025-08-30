package net.pgfmc.claims.ownable.block;

import java.util.ArrayList;

import org.bukkit.potion.PotionEffect;

public class Pair {

    public ArrayList<PotionEffect> effects;
    public int beaconCount;

    public Pair(ArrayList<PotionEffect> effects, int beaconCount) {
        this.effects = effects;
        this.beaconCount = beaconCount;
    }
}
