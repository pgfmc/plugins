package net.pgfmc.parkour.levels;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;
import net.pgfmc.parkour.events.Region;

public class RuinedCaverns {



    public static final Region act1 = new Region(new Vector4(-39, 38, -55, 0), new Vector4(98, 98, 49, 1));

    public class RCAct1 implements Level {

        @Override
        public boolean contains(Vector4 pos) {
            return act1.contains(pos);
        }

        @Override
        public void move(Vector4 pos, PlayerData pd) {
            pd.sendMessage(getName());
        }

        @Override
        public void interact(Vector4 pos, PlayerData pd) {
            pd.sendMessage("Interacted @" + getName());

        }

        private static final String name = "Ruined Caverns Act1";
        @Override
        public String getName() {
            return name;
        }
    }













}
