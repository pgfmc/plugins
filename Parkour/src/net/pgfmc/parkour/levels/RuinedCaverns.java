package net.pgfmc.parkour.levels;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;
import net.pgfmc.parkour.events.Region;

public class RuinedCaverns {

   public static void interact(Vector4 pos, PlayerData pd) {
       pd.sendMessage("Gottem.");

   }

    public static final Region act1 = new Region(new Vector4(-39, 38, -55, 0), new Vector4(98, 98, 49, 1));










}
