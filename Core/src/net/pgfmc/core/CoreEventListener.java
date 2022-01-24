package net.pgfmc.core;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import net.pgfmc.core.requests.RequestCreateEvent;
import net.pgfmc.core.requests.RequestEndEvent;

public interface CoreEventListener extends EventListener {
	
	static final Set<CoreEventListener> listeners = new HashSet<>();
	
	default void register() {
		listeners.add(this);
	}
	
	public default void RequestCreateEvent(RequestCreateEvent e) {}
	public default void RequestEndEvent(RequestEndEvent e) {};
	
	
	
}
