package net.pgfmc.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.TranslationStore;
import net.pgfmc.core.util.files.Mixins;

public class LoadJson {


    public static Map<String, String> loadJson(File file) {


        String fileData = null;

        try {
            fileData = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            return new HashMap<String, String>();
        }





        HashMap<String, String> langaugeData = new Gson().fromJson(fileData, HashMap.class);




        return langaugeData;
    }

    public static void createTranslator(String dir) {
        File file = Mixins.getFile(dir);
        Map<String, String> map = loadJson(file);
        final TranslationStore<Component> store = TranslationStore.component(Key.key("pgf:translations"));
        store.registerAll(Locale.US, map);


    }




    
}
