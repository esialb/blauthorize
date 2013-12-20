package org.blauthorize;

import org.blauthrorize.Authorization;
import org.blauthrorize.URLAuthorization;
import org.blauthrorize.Authorization.Status;
import org.junit.Assert;
import org.junit.Test;

public class URLAuthorizationTest {
	@Test
	public void testIsAuthorized() {
		Authorization b = new URLAuthorization(URLAuthorizationTest.class.getResource("URLAuthorizationTest.json"));
		Assert.assertEquals(Status.AUTHORIZED, b.isAuthorized("robin", "foo"));
		Assert.assertEquals(Status.AUTHORIZED, b.isAuthorized("robin", "bar"));
		Assert.assertEquals(Status.UNAUTHORIZED, b.isAuthorized("robin", "qux"));

	}
}
