package org.blauthorize;

import org.apache.commons.io.FileUtils;
import org.blauthrorize.FileAuthorization;
import org.blauthrorize.MutableAuthorization;
import org.junit.Assert;
import org.junit.Test;

public class FileAuthorizationTest {
	@Test
	public void testCreateFileBlauthorization() throws Exception {
		MutableAuthorization b = new FileAuthorization(
				FileUtils.toFile(
						FileAuthorizationTest.class.getResource(
								"FileAuthorizationTest.json")));
		Assert.assertTrue(b.isAuthorized("robin", "foo"));
		Assert.assertFalse(b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", true);
		Assert.assertTrue(b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", false);
		Assert.assertFalse(b.isAuthorized("robin", "qux"));
		
	}
}
