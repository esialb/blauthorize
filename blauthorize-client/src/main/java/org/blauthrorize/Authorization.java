package org.blauthrorize;

import java.util.Set;

public interface Authorization {
	public enum Status {
		AUTHORIZED,
		UNAUTHORIZED,
		NOT_APPLICABLE,
	}
	
	public Status isAuthorized(String authToken, String authGroup);
	public Status isAuthorized(Set<String> authTokens, String authGroup);
}
