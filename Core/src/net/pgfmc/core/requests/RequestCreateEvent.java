package net.pgfmc.core.requests;

import net.pgfmc.core.CoreEventListener;

public class RequestCreateEvent {
	
	public RequestCreateEvent() {
		
		
		
		
		for (CoreEventListener cel : CoreEventListener.listeners) {
			cel.RequestCreateEvent(this);
		}
	}
	
	
	
}
