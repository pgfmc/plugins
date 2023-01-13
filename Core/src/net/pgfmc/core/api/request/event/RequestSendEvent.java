package net.pgfmc.core.api.request.event;

import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.event.handler.RequestListener;
import net.pgfmc.core.api.request.event.handler.RequestListenerHandler;

public class RequestSendEvent extends RequestEvent {

	public RequestSendEvent(Request request) {
		super(request);
		
		for (RequestListener list : RequestListenerHandler.getListeners()) {
			list.onRequestSend(this);
			
		}
		
	}
	
}
