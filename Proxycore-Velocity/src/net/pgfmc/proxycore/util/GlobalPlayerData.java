package net.pgfmc.proxycore.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.moandjiezana.toml.Toml;

public class GlobalPlayerData {
    
    public static String dataPath = "/pgf/proxied-test/velocity/plugins/PGF-Proxycore" + File.separator.toString() + "playerdata" + File.separator.toString();

    public static String getDiscordID(UUID uuid) {

        Path path = Path.of(dataPath + uuid.toString() + ".toml");

        try {
            Toml file = new Toml().read(Files.readString(path));
            return file.getString("discord");

        } catch (IOException ex) {
            return null;
        }
    }

    public static String getNickname(UUID uuid) {

        Path path = Path.of(dataPath + uuid.toString() + ".toml");

        try {
            Toml file = new Toml().read(Files.readString(path));
            return file.getString("nick");

        } catch (IOException ex) {
            return null;
        }
    }

    public static void setData(UUID uuid, String id, String nick) {

        String path = dataPath + uuid.toString() + ".toml";
        String data = "";

        if (id != null) {
            data = "discord = \"" + id + "\"";
        }

        if (id != null && nick != null) {
            data = data + "\n";
        }

        if (nick != null) {
            data = data + "nick = \"" + nick + "\"";
        }

        File file = new File(path);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            fw.write(data);
            fw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
