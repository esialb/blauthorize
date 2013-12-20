package org.blauthrorize;

import java.util.Collections;
import java.util.Set;

public class Blauthorizations {
	public static Blauthorization or(final Blauthorization... auths) {
		return new Blauthorization() {
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				for(Blauthorization auth : auths)
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
	
	public static Blauthorization and(final Blauthorization... auths) {
		return new Blauthorization() {
			@Override
			public boolean isAuthorized(Set<String> authTokens, String authGroup) {
				for(Blauthorization auth : auths)
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
	
	public static Blauthorization not(final Blauthorization auth) {
		return new Blauthorization() {
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
	
	public static Blauthorization always() {
		return new Blauthorization() {
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
	
	public static Blauthorization never() {
		return new Blauthorization() {
			
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
	
	private Blauthorizations() {}
}
