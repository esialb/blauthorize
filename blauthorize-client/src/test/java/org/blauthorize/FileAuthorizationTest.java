package org.blauthorize;

import java.util.Collections;
import java.util.Set;

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
		Set<String> robin = Collections.singleton("robin");
		Assert.assertEquals(true, b.isAuthorized(robin, "foo", null));
		Assert.assertEquals(false, b.isAuthorized(robin, "qux", null));
		b.setAuthorized("robin", "qux", true);
		Assert.assertEquals(true, b.isAuthorized(robin, "qux", null));
		b.setAuthorized("robin", "qux", false);
		Assert.assertEquals(false, b.isAuthorized(robin, "qux", null));
		
	}
}
