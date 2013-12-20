package org.blauthorize.jdbc;

public class JdbcMembership {
	private Integer parent_gid;
	private Integer child_gid;
	public Integer getParent_gid() {
		return parent_gid;
	}
	
	public void setParent_gid(Integer parent_gid) {
		this.parent_gid = parent_gid;
	}
	public Integer getChild_gid() {
		return child_gid;
	}
	public void setChild_gid(Integer child_gid) {
		this.child_gid = child_gid;
	}
	
	
}
