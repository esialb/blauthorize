package org.blauthorize.jdbc;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.blauthrorize.MutableBlauthorization;

public class JdbcBlauthorization implements MutableBlauthorization {

	protected SqlSessionFactory fact;
	
	public JdbcBlauthorization(SqlSessionFactory sf) {
		this.fact = sf;
	}
	
	public JdbcBlauthorization(SqlSessionFactories sf) {
		fact = sf.createSqlSessionFactory();
	}
	
	@Override
	public boolean isAuthorized(String authToken, String authGroup) {
		SqlSession session = fact.openSession();
		try {
			JdbcGroup g = session.getMapper(GroupMapper.class).forName(authToken);
			if(g == null)
				return false;
			Integer authTokenId = g.getGid();
			
			g = session.getMapper(GroupMapper.class).forName(authGroup);
			if(g == null)
				return false;
			Integer authGroupId = g.getGid();
			
			Deque<Integer> pending = new ArrayDeque<Integer>();
			Set<Integer> authorized = new TreeSet<Integer>();
			pending.offer(authTokenId);
			
			MembershipMapper mm = session.getMapper(MembershipMapper.class);
			
			while(pending.size() > 0) {
				Integer group = pending.poll();
				if(group.intValue() == authGroupId.intValue())
					return true;
				if(!authorized.add(group))
					continue;

				for(JdbcMembership m : mm.forParentId(group)) {
					if(!authorized.contains(m.getChild_gid()))
						pending.offer(m.getChild_gid());
				}
			}
			
			return false;
		} finally {
			session.close();
		}
	}

	@Override
	public void setAuthorized(String authToken, String authGroup, boolean authorized) {
		SqlSession session = fact.openSession();
		try {
			IdMapper ids = session.getMapper(IdMapper.class);
			GroupMapper groups = session.getMapper(GroupMapper.class);
			MembershipMapper members = session.getMapper(MembershipMapper.class);
			
			JdbcGroup ag = groups.forName(authToken);
			JdbcGroup gg = groups.forName(authGroup);
			
			if(ag == null) {
				ag = new JdbcGroup();
				ag.setName(authToken);
				ag.setGid(ids.get());
				ids.set(ag.getGid() + 1);
				groups.insert(ag);
			}
			
			if(gg == null) {
				gg = new JdbcGroup();
				gg.setName(authGroup);
				gg.setGid(ids.get());
				ids.set(gg.getGid() + 1);
				groups.insert(gg);
			}
			
			JdbcMembership m = new JdbcMembership();
			m.setParent_gid(ag.getGid());
			m.setChild_gid(gg.getGid());
			if(members.forMembership(m) == null)
				members.insert(m);
		} finally {
			session.close();
		}
		
	}
	
}