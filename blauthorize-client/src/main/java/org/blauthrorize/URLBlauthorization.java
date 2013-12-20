package org.blauthrorize;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class URLBlauthorization implements Blauthorization {

	public URLBlauthorization(URL url) {
		if(url == null)
			throw new NullPointerException();
		this.url = url;
	}
	
	protected URL url;
	
	protected Map<String, List<String>> loadGroups(InputStream in) throws IOException {
		Map<String, List<String>> groups = new TreeMap<String, List<String>>();
		
		JsonFactory jf = new JsonFactory();
		JsonParser j = jf.createParser(in);
		if(j.nextToken() != JsonToken.START_OBJECT)
			throw new IllegalStateException();
		while(j.nextToken() == JsonToken.FIELD_NAME) {
			String groupId = j.getCurrentName();
			if(j.nextToken() != JsonToken.START_ARRAY)
				throw new IllegalStateException();
			List<String> subgroups = new ArrayList<String>();
			while(j.nextToken() != JsonToken.END_ARRAY)
				subgroups.add(j.getText());
			groups.put(groupId, subgroups);
		}
		
		if(j.getCurrentToken() != JsonToken.END_OBJECT)
			throw new IllegalStateException();
		
		return groups;
	}
	
	@Override
	public boolean isAuthorized(String authToken, String authGroup) {
		Map<String, List<String>> groups;
		
		try {
			groups = loadGroups(url.openStream());
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
		
		Deque<String> pending = new ArrayDeque<String>();
		Set<String> authorized = new TreeSet<String>();
		pending.offer(authToken);
		while(pending.size() > 0) {
			String group = pending.poll();
			if(group.equals(authGroup))
				return true;
			if(!authorized.add(group))
				continue;
			if(!groups.containsKey(group))
				continue;
			for(String g : groups.get(group)) {
				if(!authorized.contains(g))
					pending.offer(g);
			}
		}
		
		return false;
	}

}
