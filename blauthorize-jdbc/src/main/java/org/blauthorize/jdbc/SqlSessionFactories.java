package org.blauthorize.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactories {
	protected Properties props;
	
	public SqlSessionFactories(Properties props) {
		this.props = props;
	}
	
	public SqlSessionFactories(URL props) throws IOException {
		this(new Properties());
		InputStream in = props.openStream();
		try {
			this.props.load(in);
		} finally {
			in.close();
		}
	}
	
	public SqlSessionFactory createSqlSessionFactory() {
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		return builder.build(SqlSessionFactories.class.getResourceAsStream("mybatis.xml"), props);
	}
}
