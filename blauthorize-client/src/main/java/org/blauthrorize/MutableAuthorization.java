package org.blauthrorize;

public interface MutableAuthorization extends Authorization {
	public void setAuthorized(String authToken, String authGroup, boolean authorized);
}
