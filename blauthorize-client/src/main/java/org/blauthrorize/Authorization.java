package org.blauthrorize;

import java.util.Set;

public interface Authorization {
	public boolean isAuthorized(Set<String> authTokens, String authGroup, String groupSecret);
}
