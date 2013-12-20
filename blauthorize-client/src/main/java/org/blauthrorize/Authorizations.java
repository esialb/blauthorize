package org.blauthrorize;

import java.util.Collections;
import java.util.Set;

public class Authorizations {
	public static Authorization or(final Authorization... auths) {
		return new Authorization() {
			@Override
			public Status isAuthorized(Set<String> authTokens, String authGroup) {
				Status stat = Status.NOT_APPLICABLE;
				for(Authorization auth : auths) {
					Status s = auth.isAuthorized(authTokens, authGroup);
					if(s == Status.AUTHORIZED)
						return Status.AUTHORIZED;
					if(s == Status.UNAUTHORIZED)
						stat = Status.UNAUTHORIZED;
				}
				return stat;
			}
			
			@Override
			public Status isAuthorized(String authToken, String authGroup) {
				return isAuthorized(Collections.singleton(authToken), authGroup);
			}
		};
	}
	
	public static Authorization and(final Authorization... auths) {
		return new Authorization() {
			@Override
			public Status isAuthorized(Set<String> authTokens, String authGroup) {
				Status stat = Status.NOT_APPLICABLE;
				for(Authorization auth : auths) {
					Status s = auth.isAuthorized(authTokens, authGroup);
					if(s == Status.UNAUTHORIZED)
						return Status.UNAUTHORIZED;
					if(s == Status.AUTHORIZED)
						stat = Status.AUTHORIZED;
				}
				return stat;
			}
			
			@Override
			public Status isAuthorized(String authToken, String authGroup) {
				return isAuthorized(Collections.singleton(authToken), authGroup);
			}
		};
	}
	
	public static Authorization not(final Authorization auth) {
		return new Authorization() {
			@Override
			public Status isAuthorized(Set<String> authTokens, String authGroup) {
				Status stat = auth.isAuthorized(authTokens, authGroup);
				if(stat == Status.AUTHORIZED)
					return Status.UNAUTHORIZED;
				if(stat == Status.UNAUTHORIZED)
					return Status.AUTHORIZED;
				return Status.NOT_APPLICABLE;
			}
			
			@Override
			public Status isAuthorized(String authToken, String authGroup) {
				return isAuthorized(Collections.singleton(authToken), authGroup);
			}
		};
	}
	
	public static Authorization always() {
		return new Authorization() {
			@Override
			public Status isAuthorized(Set<String> authTokens, String authGroup) {
				return Status.AUTHORIZED;
			}
			
			@Override
			public Status isAuthorized(String authToken, String authGroup) {
				return Status.AUTHORIZED;
			}
		};
	}
	
	public static Authorization never() {
		return new Authorization() {
			
			@Override
			public Status isAuthorized(Set<String> authTokens, String authGroup) {
				return Status.UNAUTHORIZED;
			}
			
			@Override
			public Status isAuthorized(String authToken, String authGroup) {
				return Status.UNAUTHORIZED;
			}
		};
	}
	
	private Authorizations() {}
}
