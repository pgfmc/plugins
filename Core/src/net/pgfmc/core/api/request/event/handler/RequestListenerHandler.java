package net.pgfmc.core.api.request.event.handler;

import java.util.HashSet;
import java.util.Set;

public class RequestListenerHandler {
	
	private static Set<RequestListener> listeners = new HashSet<>();
	
	public static void register(RequestListener list) {
		listeners.add(list);
	}
	
	public static Set<RequestListener> getListeners() {
		return listeners;
	}
	
}
