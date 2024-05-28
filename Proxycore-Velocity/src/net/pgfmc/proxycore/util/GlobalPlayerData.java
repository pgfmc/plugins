package net.pgfmc.proxycore.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.moandjiezana.toml.Toml;

import net.pgfmc.proxycore.Main;

public class GlobalPlayerData {
    

    public UUID playerUuid;
    public String playerNick;
    public String discordID;

    public static void setup() {

        UUID me = UUID.fromString("8ebdc17a-8225-4b6f-8d49-e761fbfb5741");
    
        Path path = Main.plugin.dataDirectory;

        String pathname = "/pgf/proxied-test/velocity/plugins/Proxycore-Velocity" + File.separator.toString() + "playerdata" + File.separator.toString() + me + ".toml";

        path = Path.of(pathname);

        Logger.log(path.toString());



        try {
            Toml file = new Toml().read(Files.readString(path));
            String discord = file.getString("discord");
            String nick = file.getString("nick");
            Logger.log("discord: " + discord + " and nick: " + nick); 

        } catch (IOException ex) {
            Logger.error("Failed to load playerdata file!");
        }




        //Logger.error(input.toString());

        //System.out.println(input);






    }
}
