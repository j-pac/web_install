package com.example.connectiontoservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R.raw;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String WEB_SERVICE_URL = "http://10.0.2.2/registry_service.php";
	private static final String USERNAME = "galrito";
	private static final String DEVICE = "123";

	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final String DEVICE_ID_KEY = "device_imei";

	private String usertoken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new SendPostTask().execute(WEB_SERVICE_URL, USERNAME, DEVICE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class SendPostTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... user_param) {
			InputStream in = RegistryHttpPOSTConnection(user_param[0],
					user_param[1], user_param[2]);

			String response = null;
			try {
				response = getResponse(in);
			} catch (IOException e) {
				Log.e("log_tag", "Error in http connection ");
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Response is: " + result,
					Toast.LENGTH_LONG).show();
		}

		// ---Connects using HTTP POST---
		private InputStream RegistryHttpPOSTConnection(String url,
				String username, String device_id) {
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

}
