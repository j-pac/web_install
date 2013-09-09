package com.aptoide.androidclient;


import android.app.Application;
import android.content.Intent;
import android.widget.Toast;


public class AndroidClientApplication extends Application {
	
	@Override
	public void onCreate() {
		Intent intent = new Intent(this, RabbitMqHandlerService.class);
		startService(intent);
		Toast.makeText(this, "Adroid client application started", Toast.LENGTH_LONG).show();
	}
	
	
}
