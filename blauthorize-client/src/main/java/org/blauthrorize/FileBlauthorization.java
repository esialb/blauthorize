package org.blauthrorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class FileBlauthorization implements MutableBlauthorization {

	protected File file;
	
	protected Map<String, String[]> loadGroups(InputStream in) throws IOException {
		Map<String, String[]> groups = new TreeMap<String, String[]>();
		
		JsonFactory jf = new JsonFactory();
		JsonParser j = jf.createParser(in);
		j.nextToken();
		while(j.nextToken() == JsonToken.FIELD_NAME) {
			String groupId = j.getCurrentName();
			String[] subGroups = j.readValueAs(String[].class);
			groups.put(groupId, subGroups);
		}
		
		return groups;
	}
	
	@Override
	public boolean isAuthorized(String authToken, String authGroup) {
		Map<String, String[]> groups;
		
		try {
			groups = loadGroups(new FileInputStream(file));
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
		
		Deque<String> pending = new ArrayDeque<String>();
		Set<String> authorized = new TreeSet<String>();
		pending.offer(authToken);
		while(pending.size() > 0) {
			String group = pending.poll();
			if(!authorized.add(group))
				continue;
			for(String g : groups.get(group)) {
				if(!authorized.contains(g))
					pending.offer(g);
			}
		}
		
		return authorized.contains(authGroup);
	}

	@Override
	public void setAuthorized(String authToken, String authGroup, boolean authorized) {
		// TODO Auto-generated method stub
		
	}

}
