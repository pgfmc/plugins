package net.pgfmc.parkour.levels;

import net.pgfmc.core.util.Vector4;
import net.pgfmc.core.api.playerdata.PlayerData;

public class CharredCaves1 {

    // Script thats run, depending on the location that was clicked.



    public static void script(Vector4 pos, PlayerData pd) {

        pd.sendMessage("Location: Charred Caves Act 1");



        if (pos.equals(new Vector4(-28, 63, 1, 0))) {
            pd.sendMessage("bell rung !");
        }


    }

}
