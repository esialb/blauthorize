package org.blauthrorize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class FileBlauthorization 
extends URLBlauthorization 
implements MutableBlauthorization {

	protected File file;
	
	public FileBlauthorization(File file) throws MalformedURLException {
		super(file.toURI().toURL());
		this.file = file;
	}
	
	protected void writeGroups(OutputStream out, Map<String, List<String>> groups) 
	throws IOException {
		JsonGenerator j = new JsonFactory().createGenerator(out);
		j.writeStartObject();
		for(Map.Entry<String, List<String>> e : groups.entrySet()) {
			j.writeFieldName(e.getKey());
			j.writeStartArray();
			for(String g : e.getValue())
				j.writeString(g);
			j.writeEndArray();
		}
		j.writeEndObject();
		j.flush();
	}
	
	@Override
	public void setAuthorized(String authToken, String authGroup, boolean authorized) {
		Map<String, List<String>> groups;
		try {
			InputStream in = new FileInputStream(file);
			try {
				groups = loadGroups(in);
			} finally {
				in.close();
			}
			if(authorized && !groups.containsKey(authToken))
				groups.put(authToken, new ArrayList<String>());
			if(authorized && !groups.get(authToken).contains(authGroup))
				groups.get(authToken).add(authGroup);
			else if(!authorized && groups.get(authToken).contains(authGroup))
				groups.get(authToken).remove(authGroup);
			if(!authorized && groups.get(authToken).size() == 0)
				groups.remove(authToken);
			OutputStream out = new FileOutputStream(file);
			try {
				writeGroups(out, groups);
			} finally {
				out.close();
			}
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

}
