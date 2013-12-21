package org.blauthrorize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class HttpAuthorization implements Authorization {
	
	protected URL url;
	
	public HttpAuthorization(URL url) {
		if(url == null)
			throw new NullPointerException();
		this.url = url;
	}
	
	@Override
	public boolean isAuthorized(Set<String> authTokens, String authGroup, String groupSecret) {
		try {
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			try {
				http.setDoInput(true);
				http.setDoOutput(true);
				
				JsonFactory jf = new JsonFactory();
				JsonGenerator j = jf.createGenerator(http.getOutputStream());
				
				j.writeStartObject();
				j.writeObjectField("authGroup", authGroup);
				j.writeObjectField("groupSecret", groupSecret);
				j.writeFieldName("authTokens");
				j.writeStartArray();
				for(String token : authTokens)
					j.writeString(token);
				j.writeEndArray();
				j.writeEndObject();
				j.flush();

				InputStream i = http.getInputStream();
				byte[] b = new byte[1024];
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				for(int r = i.read(b); r != -1; r = i.read(b))
					buf.write(b, 0, r);
				
				return Boolean.valueOf(new String(buf.toByteArray(), "UTF-8"));
			} finally {
				http.disconnect();
			}
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

}
