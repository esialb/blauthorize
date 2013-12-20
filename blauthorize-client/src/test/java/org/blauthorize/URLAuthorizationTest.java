package org.blauthorize;

import org.blauthrorize.Authorization;
import org.blauthrorize.URLAuthorization;
import org.junit.Assert;
import org.junit.Test;

public class URLAuthorizationTest {
	@Test
	public void testIsAuthorized() {
		Authorization b = new URLAuthorization(URLAuthorizationTest.class.getResource("URLAuthorizationTest.json"));
		Assert.assertTrue(b.isAuthorized("robin", "foo"));
		Assert.assertTrue(b.isAuthorized("robin", "bar"));
		Assert.assertTrue(!b.isAuthorized("robin", "qux"));

	}
}
