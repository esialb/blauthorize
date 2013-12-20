package org.blauthorize.jdbc;

import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

public class SqlSessionFactoriesTest {
	@Test
	public void testCreateDerby() throws Exception {
		Properties props = new Properties();
		
		props.setProperty("driver", "org.apache.derby.jdbc.EmbeddedDriver");
		props.setProperty("url", "jdbc:derby:target/sql-session-factories-test;create=true");
		props.setProperty("username", "fake");
		props.setProperty("password", "fake");
		
		SqlSessionFactories f = new SqlSessionFactories(props);
		SqlSessionFactory fact = f.createSqlSessionFactory();
		f.createTables(fact);
	}
}
