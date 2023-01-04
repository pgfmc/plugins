package net.pgfmc.core.api.request.event;

import net.pgfmc.core.api.playerdata.PlayerData;
import net.pgfmc.core.api.request.Request;
import net.pgfmc.core.api.request.RequestType;

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
