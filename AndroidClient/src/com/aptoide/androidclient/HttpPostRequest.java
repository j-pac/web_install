package com.aptoide.androidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public abstract class HttpPostRequest extends AsyncTask<String, Void, String> {

	private String uri;
	
	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	private void processRequest(String... args) {
		
	}
	
	
	private InputStream openHttpPOSTConnection(String url, String username,
			String device_id) {
		InputStream input_stream = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			// ---set the headers---
			httpPost.addHeader("Host", "www.webservicex.net");
			httpPost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			// ---the key/value pairs to post to the server---

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair(USERNAME_KEY,
					username));
			// nameValuePairs.add(new BasicNameValuePair(PASSWORD_KEY,
			// Password));
			nameValuePairs.add(new BasicNameValuePair(DEVICE_ID_KEY,
					device_id));

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpclient.execute(httpPost);
			input_stream = httpResponse.getEntity().getContent();
		} catch (Exception e) {
			Log.d("RegistryHttpPOSTConnection", e.getLocalizedMessage());
		}
		return input_stream;
	}

	private String getResponse(InputStream in) throws IOException {
		String response = null;

		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;

			while ((line = rd.readLine()) != null) {
				str.append(line + "\n");
			}

			response = str.toString();

		} finally {
			rd.close();
		}
		return response;
	}


	

}


