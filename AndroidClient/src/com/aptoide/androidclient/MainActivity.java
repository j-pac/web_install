package com.aptoide.androidclient;

import android.os.Bundle;
import android.os.IBinder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int LOGIN_REQUEST_CODE = 1;
	public static final String QUEUE_ID_STRING = "queue_id";

	private static User user_logged;
	private static boolean device_associated_with_user_account;
	private static String queue_routing_key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// if device is currently associated with aptoide account, starts
		// RabbitMq
		// synchronous Service
		if (UserSessionManager.isDeviceAlreadyAssociated(this)) {
			Intent intent = new Intent(getApplicationContext(),
					RabbitMqHandlerService.class);
			startService(intent);
		}

		Toast.makeText(this, "APLICAÇAO INICIADA = ", Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == LOGIN_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				queue_routing_key = data.getStringExtra(QUEUE_ID_STRING);
			}
		}
	}

	public void login(View view) {

		Intent intent = new Intent(this, LoginActivity.class);
		// intent.putExtra(UserSessionManager.PREF_NAME, session_manager);

		startActivity(intent);

		// Intent intent = new
		// Intent("com.aptoide.androidclient.LoginActivity");
		// startActivity(Intent.createChooser(intent, "Choose an application"));
	}

}
