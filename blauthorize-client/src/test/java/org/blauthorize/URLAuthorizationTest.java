package org.blauthorize;

import java.util.Collections;
import java.util.Set;

import org.blauthrorize.Authorization;
import org.blauthrorize.URLAuthorization;
import org.junit.Assert;
import org.junit.Test;

public class URLAuthorizationTest {
	@Test
	public void testIsAuthorized() {
		Authorization b = new URLAuthorization(URLAuthorizationTest.class.getResource("URLAuthorizationTest.json"));
		Set<String> robin = Collections.singleton("robin");
		Assert.assertEquals(true, b.isAuthorized(robin, "foo", null));
		Assert.assertEquals(true, b.isAuthorized(robin, "bar", null));
		Assert.assertEquals(false, b.isAuthorized(robin, "qux", null));

	}
}
