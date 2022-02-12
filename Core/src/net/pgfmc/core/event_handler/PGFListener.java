package net.pgfmc.core.event_handler;

import net.pgfmc.core.event_handler.requests.RequestEndEvent;
import net.pgfmc.core.event_handler.requests.RequestSendEvent;

public interface PGFListener {
	
	public default void onRequestSend(RequestSendEvent event) {}
	public default void onRequestEnd(RequestEndEvent event) {}
	
	
		
	
	
	
}
