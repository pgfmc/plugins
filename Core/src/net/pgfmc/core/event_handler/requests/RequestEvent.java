package net.pgfmc.core.event_handler.requests;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requests.Request;
import net.pgfmc.core.requests.RequestType;

public abstract class RequestEvent {
	
	
	private Request request;
	
	protected RequestEvent(Request request) {
		this.request = request;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public RequestType getType() {
		return request.parent;
	}
	
	public PlayerData getAsker() {
		return request.asker;
	}
	
	public PlayerData getTarget() {
		return request.target;
	}
}
