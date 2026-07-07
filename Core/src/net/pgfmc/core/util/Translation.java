package net.pgfmc.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;

import com.google.gson.Gson;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import net.pgfmc.core.util.files.Mixins;

public class Translation {

    public static final Locale DISCORD_LOCALE = new Locale.Builder().setLanguage("English").setVariant("Discord").build();


    public static void createTranslator(String dir) {

        File file = Mixins.getFile(dir);
        String fileData = null;

        try {
            fileData = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            Logger.error("Place a non-empty en_us.json file in the Core plugin data path NOW! (effective only after restart)");
            return;
        }

        HashMap<String, String> langaugeData = new Gson().fromJson(fileData, HashMap.class);

        if (langaugeData == null || langaugeData.isEmpty()) {
            Logger.error("Place a non-empty en_us.json file in the Core plugin data path NOW! (effective only after restart)");
            return;
        }

        HashMap<String, MessageFormat> messageFormatData = new HashMap<>();

        for (String key : langaugeData.keySet()) {

            //if (!key.startsWith("advancement")) {
            //    continue;
            //}

            String value = langaugeData.get(key);

            //Component accumulator = Component.empty();

            //String[] things = value.split("%s");

            //for (int i = 0; i < things.length; i++) {
            //    accumulator.append(Component.arguments().get(i));
            //    
            //}



            messageFormatData.put(key, new MessageFormat(value, DISCORD_LOCALE));
            //Logger.log(key + " : " + value);
        }
        
        final TranslationStore<MessageFormat> messageFormatStore = TranslationStore.messageFormat(Key.key("pgf:translations"));
        messageFormatStore.registerAll(DISCORD_LOCALE, messageFormatData);


        GlobalTranslator.translator().addSource(messageFormatStore);
    }
}
