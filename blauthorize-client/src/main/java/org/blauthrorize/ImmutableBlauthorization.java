package org.blauthrorize;

import java.util.Set;

public class ImmutableBlauthorization implements Blauthorization {

	private Blauthorization blauth;
	
	public ImmutableBlauthorization(Blauthorization auth) {
		this.blauth = auth;
	}

	public boolean isAuthorized(String authToken, String authGroup) {
		return blauth.isAuthorized(authToken, authGroup);
	}

	public boolean isAuthorized(Set<String> authTokens, String authGroup) {
		return blauth.isAuthorized(authTokens, authGroup);
	}
	

}
