package org.blauthorize;

import org.blauthrorize.Blauthorization;
import org.blauthrorize.URLBlauthorization;
import org.junit.Assert;
import org.junit.Test;

public class URLBlauthorizationTest {
	@Test
	public void testIsAuthorized() {
		Blauthorization b = new URLBlauthorization(URLBlauthorizationTest.class.getResource("URLBlauthorizationTest.json"));
		Assert.assertTrue(b.isAuthorized("robin", "foo"));
		Assert.assertTrue(b.isAuthorized("robin", "bar"));
		Assert.assertTrue(!b.isAuthorized("robin", "qux"));

	}
}
