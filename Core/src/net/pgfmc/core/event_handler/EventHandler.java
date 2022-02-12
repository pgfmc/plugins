package net.pgfmc.core.event_handler;

import java.util.HashSet;
import java.util.Set;

public class EventHandler {
	
	private static Set<PGFListener> listeners = new HashSet<>();
	
	public static void register(PGFListener list) {
		listeners.add(list);
	}
	
	public static Set<PGFListener> getListeners() {
		return listeners;
	}
}
