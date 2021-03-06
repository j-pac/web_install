package com.aptoide.androidclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
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
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class LoginActivity extends Activity {

	private static final String WEB_SERVICE_URL = "http://10.0.2.2/registry_service.php";
	private static final String USERNAME = "tintim";

	private UserSessionManager session_manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();

		// get user session manager from main activity
		// session_manager = (UserSessionManager) getIntent()
		// .getSerializableExtra(UserSessionManager.PREF_NAME);
		session_manager = new UserSessionManager(getBaseContext());

		// chose between loggin and loggout layout
		// if (session_manager.isLoggedIn()) {
		if (true) {
			setContentView(R.layout.activity_login);
		} else {
			setContentView(R.layout.activity_logout);
			TextView show_email = (TextView) findViewById(R.id.show_email);
			// show_email.setText(session_manager.);
		}

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	private String checkLoginCredentialsInDatabase(String email, String password)
//			throws SQLException {
//		AptoideClientDatabaseConnection database_connection = new AptoideClientDatabaseConnection();
//
//		String token = database_connection.LoginUser(email, password);
//
//		database_connection.CloseConnection();
//
//		return token;
//	}

	public void doLogin(View view) {

		String email = ((EditText)findViewById(R.id.email_edit)).getText().toString();
		String password = ((EditText)findViewById(R.id.password_edit)).getText().toString();
				
//		if (email.trim().length() > 0 && password.trim().length() > 0) {
//			if (!email.contains("@") || !email.contains(".")) {
//				// alert invalid email, clean fields and ask again
//			} else {
//				// send login request
//			}
//		}
		
		
		// DUMMY STUFF
//		if(email.equals(USERNAME)) {
//			Intent i = new Intent(this, RabbitMqHandlerService.class);
//			startService(i);
//			Toast.makeText(getBaseContext(), "SErvice started", Toast.LENGTH_LONG).show();
//		} else {
//			Toast.makeText(getBaseContext(), "Not " + email, Toast.LENGTH_SHORT).show();
//		}

		// Save user's session if login data were correct and had succeed
		// without
		// exceptions
//		try {
//			String usertoken = checkLoginCredentialsInDatabase(email, password);
//
//			if (usertoken != null) {
//				// filling userdata in shared preferences
//				session_manager.createLoginSession(email, usertoken);
//				// check if user wants to register this device
//				if (((CheckBox) findViewById(R.id.associate_device))
//						.isChecked()) {
//					// send an http request to regisry_service.php and if have
//					// exit, save the response and start the service to listen RabbitMq
//				}
//				Toast.makeText(this, "Login succesfully as: " + email,
//						Toast.LENGTH_LONG).show();
//			} else {
//				// PRESENT A WRONG'S USER CREDENTIALS DIALOG
//			}
//		} catch (SQLException e) {
//			Toast.makeText(this, "Error while communicating with database",
//					Toast.LENGTH_LONG).show();
//		}

		// Send an http request to database to authenticate the user, and
		// receive the token

		// if client is authenticate, and wish to associate his device with his
		// account, send a request to the web service

		// if response is OK, save the queue_id, and start the service to get
		// messages from rabbitMq

		// Call to checkUserCredential web service

		// --------------------- FAZER CONEXAO À BASE DE DADOS DEPOIS
		// -----------------------------------

		// // check if this user exists in Postgres database
		// JDBCPostgresConnection db_conn = new JDBCPostgresConnection();
		//
		// // if login had success, complete user's usertoken from response
		// // user.setUsertoken(usertoken);
		//
		//
		// // close db connection
		// db_conn.closeConnection();

		// if "associate device with account" is checked, associate device with
		// user account
//		if (((CheckBox) findViewById(R.id.associate_device)).isChecked()) {
//			// new RegistryDeviceTask().execute(WEB_SERVICE_URL, usertoken,
//			// device_serial);
//		}

		// Intent i = new Intent();
		//
		// if (queue_name != null) {
		//
		// i.putExtra("queueName", queue_name);
		// setResult(RESULT_OK, i);
		// } else {
		// setResult(RESULT_CANCELED, i);
		// }
		//
		// Toast.makeText(
		// this,
		// "authentication send: " + user.getEmail() + " , "
		// + user.getPassword(), Toast.LENGTH_SHORT).show();

//		finish();

	}

	public void doLogout(View view) {
		session_manager.logoutUser();

		setContentView(R.layout.activity_login);
	}

	// private void createUser() {
	//
	// EditText email = (EditText) findViewById(R.id.email_edit);
	// EditText password = (EditText) findViewById(R.id.password_edit);
	//
	// String email_text = email.getText().toString();
	// String password_text = password.getText().toString();
	//
	// if (email_text != null && password_text != null) {
	// if (email_text.contains("@")) {
	// user = new User(email_text, password_text);
	// }
	// }
	// }

	// private class RegistryDeviceTask extends AsyncTask<String, Void, String>
	// {
	//
	// private static final String USERNAME_KEY = "username";
	// private static final String DEVICE_ID_KEY = "device_imei";
	//
	// @Override
	// protected String doInBackground(String... conn_params) {
	// InputStream in = openHttpPOSTConnection(conn_params[0],
	// conn_params[1], conn_params[2]);
	//
	// String response = null;
	// try {
	// response = getResponse(in);
	// } catch (IOException e) {
	// Log.d("getREsponse", e.getLocalizedMessage());
	//
	// e.printStackTrace();
	// }
	// return response;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// Toast.makeText(getBaseContext(), "Response is: " + result,
	// Toast.LENGTH_LONG).show();
	//
	// queue_name = result;
	// }
	//
	// private InputStream openHttpPOSTConnection(String url, String username,
	// String device_id) {
	// InputStream input_stream = null;
	// try {
	// HttpClient httpclient = new DefaultHttpClient();
	// HttpPost httpPost = new HttpPost(url);
	// // ---set the headers---
	// httpPost.addHeader("Host", "www.webservicex.net");
	// httpPost.addHeader("Content-Type",
	// "application/x-www-form-urlencoded");
	// // ---the key/value pairs to post to the server---
	//
	// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	// 2);
	// nameValuePairs.add(new BasicNameValuePair(USERNAME_KEY,
	// username));
	// // nameValuePairs.add(new BasicNameValuePair(PASSWORD_KEY,
	// // Password));
	// nameValuePairs.add(new BasicNameValuePair(DEVICE_ID_KEY,
	// device_id));
	//
	// httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	// HttpResponse httpResponse = httpclient.execute(httpPost);
	// input_stream = httpResponse.getEntity().getContent();
	// } catch (Exception e) {
	// Log.d("RegistryHttpPOSTConnection", e.getLocalizedMessage());
	// }
	// return input_stream;
	// }
	//
	// private String getResponse(InputStream in) throws IOException {
	// String response = null;
	//
	// BufferedReader rd = null;
	// try {
	// rd = new BufferedReader(new InputStreamReader(in));
	// StringBuilder str = new StringBuilder();
	// String line = null;
	//
	// while ((line = rd.readLine()) != null) {
	// str.append(line + "\n");
	// }
	//
	// response = str.toString();
	//
	// } finally {
	// rd.close();
	// }
	// return response;
	// }
	//
	// }

	/*
	 * private class checkUserCredentials extends AsyncTask<Params, Progress,
	 * Result> {
	 * 
	 * @Override protected Result doInBackground(Params... params) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * public String PostToWebservice(String URL, )
	 * 
	 * }
	 */

}
