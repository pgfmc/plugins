package net.pgfmc.parkour;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.pgfmc.core.api.playerdata.PlayerDataManager;
import net.pgfmc.parkour.events.PlayerInteract;
import net.pgfmc.parkour.events.PlayerMove;



public class Main extends JavaPlugin {
	
    public static Main plugin;

    @Override
    public void onEnable() {

        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(), this);

        PlayerDataManager.setInit(pd -> {


                FileConfiguration db = pd.loadFile();

                if (db == null) return;

                ConfigurationSection config = db.getConfigurationSection("parkour");

                ParkourData pardata;

                if (config != null) {

                    List<Boolean> bells = config.getBooleanList("bells");
                    boolean[] bellarray = new boolean[bells.size()];

                    int order = 0;

                    for (Boolean bell : bells) {
                        bellarray[order] = bell;
                        order++;
                    }

                    pardata = new ParkourData(pd, ParkourData.Checkpoint.valueOf(config.getString("checkpoint")), bellarray );
                } else {
                    pardata = new ParkourData(pd, ParkourData.Checkpoint.START_GAME, new boolean[1]);
                }

            pd.setData("parkour", pardata);
            pd.saveToFile("parkour", pardata.toConfigSection(db));
        });
    }
}
