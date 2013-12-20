package org.blauthorize.jdbc;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.blauthrorize.MutableAuthorization;
import org.blauthrorize.Authorization.Status;
import org.junit.Assert;
import org.junit.Test;

public class JdbcAuthorizationTest {
	private static final SqlSessionFactories f;
	private static final SqlSessionFactory fact;
	
	static {
		Properties props = new Properties();

		props.setProperty("driver", "org.apache.derby.jdbc.EmbeddedDriver");
		props.setProperty("url", "jdbc:derby:memory:jdbc-blauthorization-test;create=true");
		props.setProperty("username", "");
		props.setProperty("password", "");

		f = new SqlSessionFactories(props);
		fact = f.createSqlSessionFactory();
		try {
			f.createTables(fact);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testCreateFileBlauthorization() throws Exception {
		MutableAuthorization b = new JdbcAuthorization(fact);
		
		Assert.assertEquals(Status.NOT_APPLICABLE, b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", true);
		Assert.assertEquals(Status.AUTHORIZED, b.isAuthorized("robin", "qux"));
		b.setAuthorized("robin", "qux", false);
		Assert.assertEquals(Status.UNAUTHORIZED, b.isAuthorized("robin", "qux"));
		
	}
}
