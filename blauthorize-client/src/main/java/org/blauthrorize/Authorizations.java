package org.blauthrorize;

import java.util.Collections;
import java.util.Set;

public class Authorizations {
	public static Authorization or(final Authorization... auths) {
		return new Authorization() {
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				for(Authorization auth : auths)
					if(auth.isAuthorized(authTokens, authGroup))
						return true;
				return false;
			}
			
			@Override
			public boolean isAuthorized(String authToken, String authGroup) {
				return isAuthorized(Collections.singleton(authToken), authGroup);
			}
		};
	}
	
	public static Authorization and(final Authorization... auths) {
		return new Authorization() {
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				for(Authorization auth : auths)
					if(!auth.isAuthorized(authTokens, authGroup))
						return false;
				return true;
			}
			
			@Override
			public boolean isAuthorized(String authToken, String authGroup) {
				return isAuthorized(Collections.singleton(authToken), authGroup);
			}
		};
	}
	
	public static Authorization not(final Authorization auth) {
		return new Authorization() {
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				return !auth.isAuthorized(authTokens, authGroup);
			}
			
			@Override
			public boolean isAuthorized(String authToken, String authGroup) {
				return isAuthorized(Collections.singleton(authToken), authGroup);
			}
		};
	}
	
	public static Authorization always() {
		return new Authorization() {
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				return true;
			}
			
			@Override
			public boolean isAuthorized(String authToken, String authGroup) {
				return true;
			}
		};
	}
	
	public static Authorization never() {
		return new Authorization() {
			
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				return false;
			}
			
			@Override
			public boolean isAuthorized(String authToken, String authGroup) {
				return false;
			}
		};
	}
	
	private Authorizations() {}
}
