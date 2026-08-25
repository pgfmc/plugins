package net.pgfmc.core.util;

import java.util.Locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

public class TranslatableComponentDecoder {
	
	/**
	 * Resolves translation key into a regular Component.
	 * 
	 * Uses Locale.US (English)
	 * 
	 * @param component
	 * @return
	 */
	public static Component decode(TranslatableComponent component) {
		final Component translatedComponent = GlobalTranslator.render(component, Locale.US);
		return translatedComponent;
	}
	
	/**
	 * Converts a Component into plain text.
	 * 
	 * @param component
	 * @return
	 */
	public static String translate(Component component) {
		if (component instanceof TranslatableComponent) {
			component = decode((TranslatableComponent) component);
		}
		
		final String plaintext = PlainTextComponentSerializer.plainText().serialize(component);
		return plaintext;
	}

}
