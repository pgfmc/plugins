package net.pgfmc.parkour.levels;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;

public interface Level {

    public boolean contains(Vector4 pos);

    public void interact(Vector4 pos, PlayerData pd);

    public void move(Vector4 pos, PlayerData pd);

    public String getName();




    public static Level[] levels =
    {
        new RuinedCaverns().new RCAct1()
    };


}
