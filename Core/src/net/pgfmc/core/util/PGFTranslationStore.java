package net.pgfmc.core.util;

import java.text.MessageFormat;
import java.util.Locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.AbstractTranslationStore;

public class PGFTranslationStore extends AbstractTranslationStore {


    public PGFTranslationStore() {
        super(Key.key("pgf"));
    }

    
    @Override
    public MessageFormat translate(String key, Locale locale) {

        return null;

    }


     
}
