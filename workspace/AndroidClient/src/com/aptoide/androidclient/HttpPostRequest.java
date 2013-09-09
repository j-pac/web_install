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

	private String url;
	private List<NameValuePair> nameValuePairs;

	public HttpPostRequest(String url) {
		this.url = url;
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public void addPostParameter(String key, String value) {
		nameValuePairs.add(new BasicNameValuePair(key, value));
	}

	@Override
	protected String doInBackground(String... args) {
		if (!nameValuePairs.isEmpty()) {
			InputStream in = openHttpPOSTConnection();
			return getResponse(in);
		}
		return ""; // The post request has no parameters to send
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	private InputStream openHttpPOSTConnection() {
		InputStream input_stream = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			// ---set the headers---
			httpPost.addHeader("Host", "www.webservicex.net");
			httpPost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpclient.execute(httpPost);
			input_stream = httpResponse.getEntity().getContent();
		} catch (Exception e) {
			Log.d("RegistryHttpPOSTConnection", e.getLocalizedMessage());
		}
		return input_stream;
	}

	private String getResponse(InputStream in) {
		String response = null;
		BufferedReader rd = null;

		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
