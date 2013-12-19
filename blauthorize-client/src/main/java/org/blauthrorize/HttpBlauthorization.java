package org.blauthrorize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpBlauthorization implements Blauthorization {
	
	protected URL url;
	protected String authGroup;
	protected String groupSecret;
	
	public HttpBlauthorization(URL url, String authGroup, String groupSecret) {
		this.url = url;
		this.authGroup = authGroup;
		this.groupSecret = groupSecret;
	}
	
	@Override
	public boolean isAuthorized(String authToken, String authGroup) {
		if(!this.authGroup.equals(authGroup))
			return false;
		
		try {
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			try {
				http.setDoInput(true);
				http.setDoOutput(true);

				PrintStream w = new PrintStream(http.getOutputStream(), true, "UTF-8");
				w.print("authGroup=" + URLEncoder.encode(authGroup, "UTF-8"));
				w.print("&groupSecret=" + URLEncoder.encode(groupSecret, "UTF-8"));
				w.print("&authToken=" + URLEncoder.encode(authToken, "UTF-8"));
				w.flush();

				InputStream i = http.getInputStream();
				byte[] b = new byte[1024];
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				for(int r = i.read(b); r != -1; r = i.read(b))
					buf.write(b, 0, r);
				
				return Boolean.parseBoolean(new String(buf.toByteArray(), "UTF-8"));
			} finally {
				http.disconnect();
			}
		} catch(IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

}
