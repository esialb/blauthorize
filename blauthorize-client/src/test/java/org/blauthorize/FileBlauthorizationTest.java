package org.blauthorize;

import org.apache.commons.io.FileUtils;
import org.blauthrorize.FileBlauthorization;
import org.blauthrorize.MutableBlauthorization;
import org.junit.Assert;
import org.junit.Test;

public class FileBlauthorizationTest {
	@Test
	public void testCreateFileBlauthorization() throws Exception {
		MutableBlauthorization b = new FileBlauthorization(
				FileUtils.toFile(
						FileBlauthorizationTest.class.getResource(
								"FileBlauthorizationTest.json")));
		Assert.assertTrue(b.isAuthorized("robin", "foo"));
		Assert.assertFalse(b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", true);
		Assert.assertTrue(b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", false);
		Assert.assertFalse(b.isAuthorized("robin", "qux"));
		
	}
}
