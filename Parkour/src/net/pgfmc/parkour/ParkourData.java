package net.pgfmc.parkour;

import org.bukkit.configuration.ConfigurationSection;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.util.Vector4;

public class ParkourData {

    // Data for Parkour
    //
    // Contains Flags, Playtime transient data, Items collected, as well as progress.


    public ParkourData(PlayerData pd, Checkpoint checkpoint, boolean[] bells_rang) {
        this.pd = pd;
        this.checkpoint = checkpoint;
        this.bells_rang = bells_rang;
    }

    public PlayerData pd;



    // the location recorded with the enum ISNT the location of the checkpoint banner,
    // but instead the location you are respawned at.
    public enum Checkpoint {
        START_GAME(new Vector4(0, 0, 0,0)),
        CCA1_FIRST_PLATFORM(new Vector4(0,0,0,0)),
        CCA1_SECOND_PLATFORM(new Vector4(0,0,0,0)),
        CCA1_MIDWAY(new Vector4(0,0,0,0)),
        CCA1_LEVEL_END(new Vector4(0,0,0,0)),

        CCA1_HALL(new Vector4(0,0,0,0)),
        CCA1_BALCONY(new Vector4(0,0,0,0)),
        CCA2_STAIRWAY(new Vector4(0,0,0,0)),











        ;
        private final Vector4 respawn_location;
        private Checkpoint(final Vector4 location) {
            this.respawn_location = location;
        }
        public Vector4 getLocation() {
            return respawn_location;
        }

    }

    public Checkpoint checkpoint;

    public boolean[] bells_rang;





    // This function is for displaying the values for the /dump command.
    public String toString() {
        String base = "";


        base = base + "Checkpoint: " + checkpoint.toString() + " | Bells: ";
        for (boolean bell : bells_rang) {
            if (bell) {
                base = base + "T";
            } else {
                base = base + "F";
            }
        }
        return base;
    }

    public ConfigurationSection toConfigSection(ConfigurationSection db) {

        ConfigurationSection config = db.createSection("parkour");

        config.set("bells", bells_rang);
        config.set("checkpoint", checkpoint.toString());

        return config;
    }
}
