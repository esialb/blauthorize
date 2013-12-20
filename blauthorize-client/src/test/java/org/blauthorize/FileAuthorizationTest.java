package org.blauthorize;

import org.apache.commons.io.FileUtils;
import org.blauthrorize.FileAuthorization;
import org.blauthrorize.MutableAuthorization;
import org.blauthrorize.Authorization.Status;
import org.junit.Assert;
import org.junit.Test;

public class FileAuthorizationTest {
	@Test
	public void testCreateFileBlauthorization() throws Exception {
		MutableAuthorization b = new FileAuthorization(
				FileUtils.toFile(
						FileAuthorizationTest.class.getResource(
								"FileAuthorizationTest.json")));
		Assert.assertEquals(Status.AUTHORIZED, b.isAuthorized("robin", "foo"));
		Assert.assertEquals(Status.UNAUTHORIZED, b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", true);
		Assert.assertEquals(Status.AUTHORIZED, b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", false);
		Assert.assertEquals(Status.UNAUTHORIZED, b.isAuthorized("robin", "qux"));
		
	}
}
