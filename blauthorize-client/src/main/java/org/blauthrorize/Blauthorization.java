package org.blauthrorize;

import java.util.Set;

public interface Blauthorization {
	public boolean isAuthorized(String authToken, String authGroup);
	public boolean isAuthorized(Set<String> authTokens, String authGroup);
}
