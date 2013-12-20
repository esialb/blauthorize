package org.blauthorize.jdbc;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface GroupMapper {
	@Select("select * from groups where gid = #{gid}")
	public JdbcGroup forId(Integer gid);
	
	@Select("select * from groups where name = #{name}")
	public JdbcGroup forName(String name);
	
	@Select("select * from groups where gid = #{gid} and name = #{name}")
	public JdbcGroup forGroup(JdbcGroup group);
	
	@Insert("insert into groups (gid, name) values (#{gid}, #{name})")
	public void insert(JdbcGroup group);
	
	@Delete("delete from groups where gid = #{gid}")
	public void delete(Integer gid);
}
