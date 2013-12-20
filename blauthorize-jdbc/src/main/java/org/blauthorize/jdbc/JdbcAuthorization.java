package org.blauthorize.jdbc;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Set;
import java.util.TreeSet;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.blauthrorize.MutableAuthorization;

public class JdbcAuthorization implements MutableAuthorization {

	protected SqlSessionFactory fact;
	
	public JdbcAuthorization(SqlSessionFactory sf) {
		this.fact = sf;
	}
	
	public JdbcAuthorization(SqlSessionFactories sf) {
		fact = sf.createSqlSessionFactory();
	}
	
	@Override
	public Status isAuthorized(String authToken, String authGroup) {
		return isAuthorized(Collections.singleton(authToken), authGroup);
	}
	
	@Override
	public Status isAuthorized(Set<String> authTokens, String authGroup) {
		SqlSession session = fact.openSession();
		try {
			Set<Integer> authTokenIds = new TreeSet<Integer>();

			for(String token : authTokens) {
				JdbcGroup g = session.getMapper(GroupMapper.class).forName(token);
				if(g != null)
					authTokenIds.add(g.getGid());
			}
			
			if(authTokenIds.size() == 0)
				return Status.NOT_APPLICABLE;
			
			JdbcGroup g = session.getMapper(GroupMapper.class).forName(authGroup);
			if(g == null)
				return Status.UNAUTHORIZED;
			Integer authGroupId = g.getGid();
			
			Deque<Integer> pending = new ArrayDeque<Integer>();
			Set<Integer> authorized = new TreeSet<Integer>();
			pending.addAll(authTokenIds);
			
			MembershipMapper mm = session.getMapper(MembershipMapper.class);
			
			while(pending.size() > 0) {
				Integer group = pending.poll();
				if(group.intValue() == authGroupId.intValue())
					return Status.AUTHORIZED;
				if(!authorized.add(group))
					continue;

				for(JdbcMembership m : mm.forParentId(group)) {
					if(!authorized.contains(m.getChild_gid()))
						pending.offer(m.getChild_gid());
				}
			}
			
			return Status.UNAUTHORIZED;
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
				session.commit();
			}
			
			if(gg == null) {
				gg = new JdbcGroup();
				gg.setName(authGroup);
				gg.setGid(ids.get());
				ids.set(gg.getGid() + 1);
				groups.insert(gg);
				session.commit();
			}
			
			JdbcMembership m = new JdbcMembership();
			m.setParent_gid(ag.getGid());
			m.setChild_gid(gg.getGid());
			if(authorized && members.forMembership(m) == null) {
				members.insert(m);
				session.commit();
			} else if(!authorized && members.forMembership(m) != null) {
				members.delete(m);
				session.commit();
			}
		} finally {
			session.close();
		}
		
	}
	
}
