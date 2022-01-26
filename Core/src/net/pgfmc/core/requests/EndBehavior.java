package net.pgfmc.core.requests;

public enum EndBehavior {
	ACCEPT, // If the Request was accepted normally
	DENIED,     // If the Request was rejected
	FORCEEND,   // If the Reqeust was Force ended, possibly by an admin or an automatic force end by the implementer?
	REFRESH,    // If the Request was ended due to a new duplicate request being sent :)
	TIMEOUT,    // If the Request was timed out (only on timed requests)
	QUIT        // If one of the participants quit out (only on non-persistent requests)
}
