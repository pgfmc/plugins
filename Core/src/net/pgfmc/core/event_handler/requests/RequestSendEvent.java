package net.pgfmc.core.event_handler.requests;

import net.pgfmc.core.event_handler.EventHandler;
import net.pgfmc.core.event_handler.PGFListener;
import net.pgfmc.core.requests.Request;

public class RequestSendEvent extends RequestEvent {

	public RequestSendEvent(Request request) {
		super(request);
		
		for (PGFListener list : EventHandler.getListeners()) {
			list.onRequestSend(this);
		}
	}
}
