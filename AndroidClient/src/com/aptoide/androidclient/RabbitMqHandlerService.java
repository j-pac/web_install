package com.aptoide.androidclient;


import java.net.URI;
import java.net.URISyntaxException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RabbitMqHandlerService extends Service {

	private static final String TAG = "com.aptoide.androidclient";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Service RabbitMqHandlerService created!");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service RabbitMqHandlerService onStartCommand!");

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service RabbitMqHandlerService destroyed!");
	}

	private class DownloadAPP implements Runnable {
		
		private URI uri;
		
		public DownloadAPP(String spec) {
			try {
				URI uri = new URI(spec);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

	}

	private class InstallAPP implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

	}

}
