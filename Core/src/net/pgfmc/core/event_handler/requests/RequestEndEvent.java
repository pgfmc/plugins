package net.pgfmc.core.event_handler.requests;

import net.pgfmc.core.event_handler.EventHandler;
import net.pgfmc.core.event_handler.PGFListener;
import net.pgfmc.core.requests.EndBehavior;
import net.pgfmc.core.requests.Request;

public class RequestEndEvent extends RequestEvent {
	
	private EndBehavior endB;

	public RequestEndEvent(Request request, EndBehavior endB) {
		super(request);
		
		this.endB = endB;
		
		
		for (PGFListener list : EventHandler.getListeners()) {
			list.onRequestEnd(this);
		}
	}
	
	public EndBehavior getResult() {
		return endB;
	}
}
