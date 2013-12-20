package org.blauthorize.jdbc;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface IdMapper {
	@Select("select id as value from ids")
	public Integer get();
	
	@Update("update ids set id = #{id}")
	public void set(Integer id);
}