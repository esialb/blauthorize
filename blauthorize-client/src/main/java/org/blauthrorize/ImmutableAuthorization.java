package org.blauthrorize;

import java.util.Set;

public class ImmutableAuthorization implements Authorization {

	private Authorization blauth;
	
	public ImmutableAuthorization(Authorization auth) {
		this.blauth = auth;
	}

	public Status isAuthorized(String authToken, String authGroup) {
		return blauth.isAuthorized(authToken, authGroup);
	}

	public Status isAuthorized(Set<String> authTokens, String authGroup) {
		return blauth.isAuthorized(authTokens, authGroup);
	}
	

}
