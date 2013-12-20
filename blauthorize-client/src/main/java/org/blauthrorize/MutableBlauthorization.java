package org.blauthrorize;

public interface MutableBlauthorization extends Blauthorization {
	public void setAuthorized(String authToken, String authGroup, boolean authorized);
}
