package org.blauthorize.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactories {
	private static final String CREATE;
	private static final String CREATE_NOPK;
	static {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			InputStream in = SqlSessionFactories.class.getResourceAsStream("create.sql");
			for(int r = in.read(buf); r != -1; r = in.read(buf))
				bout.write(buf, 0, r);
			
			CREATE = new String(bout.toByteArray(), "UTF-8");
			
			bout.reset();
			in = SqlSessionFactories.class.getResourceAsStream("create_nopk.sql");
			for(int r = in.read(buf); r != -1; r = in.read(buf))
				bout.write(buf, 0, r);
			
			CREATE_NOPK = new String(bout.toByteArray(), "UTF-8");
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
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
	
	public void createTables(SqlSessionFactory factory) throws SQLException {
		SqlSession session = factory.openSession();
		try {
			try {
				session.getConnection().nativeSQL(CREATE);
			} catch(SQLException e) {
				session.getConnection().nativeSQL(CREATE_NOPK);
			}
		} finally {
			session.close();
		}
	}
}
