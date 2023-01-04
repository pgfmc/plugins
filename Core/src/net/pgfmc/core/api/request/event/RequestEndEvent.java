package net.pgfmc.core.api.request.event;

import net.pgfmc.core.api.request.EndBehavior;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.event.handler.RequestListener;
import net.pgfmc.core.api.request.event.handler.RequestListenerHandler;

public class RequestEndEvent extends RequestEvent {
	
	private EndBehavior endB;

	public RequestEndEvent(Request request, EndBehavior endB) {
		super(request);
		
		this.endB = endB;
		
		
		for (RequestListener list : RequestListenerHandler.getListeners()) {
			list.onRequestEnd(this);
		}
		
	}
	
	public EndBehavior getResult() {
		return endB;
	}
	
}
