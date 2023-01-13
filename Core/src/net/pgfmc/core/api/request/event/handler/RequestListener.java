package net.pgfmc.core.api.request.event.handler;

import net.pgfmc.core.api.request.event.RequestEndEvent;
import net.pgfmc.core.api.request.event.RequestSendEvent;

public interface RequestListener {
	
	public default void onRequestSend(RequestSendEvent event) {}
	public default void onRequestEnd(RequestEndEvent event) {}
	
}
