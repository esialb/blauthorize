package org.blauthorize.jdbc;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface MembershipMapper {
	@Select("select * from memberships where parent_gid = #{gid}")
	public List<JdbcMembership> forParentId(Integer gid);
	
	@Select("select * from memberships where child_gid = #{gid}")
	public List<JdbcMembership> forChildId(Integer gid);
	
	@Select("select * from memberships where parent_gid = #{parent_gid} and child_gid = #{child_gid}")
	public JdbcMembership forMembership(JdbcMembership membership);
	
	@Insert("insert into memberships (parent_gid, child_gid) values (#{parent_gid}, #{child_gid})")
	public void insert(JdbcMembership group);
	
	@Delete("delete from memberships where parent_gid = #{parent_gid} and child_gid = #{child_gid}")
	public void delete(JdbcMembership membership);

}
