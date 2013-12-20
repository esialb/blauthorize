package org.blauthorize.jdbc;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;

public class IdMapperTest {
	private static final SqlSessionFactories f;
	private static final SqlSessionFactory fact;
	
	static {
		Properties props = new Properties();

		props.setProperty("driver", "org.apache.derby.jdbc.EmbeddedDriver");
		props.setProperty("url", "jdbc:derby:memory:id-mapper-test;create=true");
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
	public void testIds() throws Exception {

		SqlSession s = fact.openSession();
		try {
			IdMapper idm = s.getMapper(IdMapper.class);
			Integer id = idm.get();
			idm.set(id + 1);
			Assert.assertEquals((Object) (id + 1), idm.get());
		} finally {
			s.close();
		}
	}
}
