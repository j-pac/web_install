package com.aptoide.androidclient;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class RabbitMqHandlerService extends Service {

	
	private static final String TAG = "com.aptoide.androidclient";
	private static final String HANDLER_THREAD_NAME = "rabbitmqhandler";

	private final String queue_routing_key = "web_install_4d4e2d90c4d6eec405d82ecc795a7e3592df1523";

	private RabbitMqClient rabbitmq_client;

	private Thread rabbitMq_pull_thread;
	
	
	private boolean isRunning = false; 

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Service RabbitMqHandlerService created!");
		
		isRunning = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service RabbitMqHandlerService onStartCommand!");

		rabbitMq_pull_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				rabbitmq_client = new RabbitMqClient(queue_routing_key);
				rabbitmq_client.establishConnection();

				while (isRunning) {
					final String message_received = rabbitmq_client
							.consumeMessage();

					new Thread(new DownloadRequest(message_received));

					// Toast.makeText(getApplicationContext(),
					// "message received: " + message_received,
					// Toast.LENGTH_SHORT).show();

				}
			}
		});

		rabbitMq_pull_thread.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service RabbitMqHandlerService destroyed!");
	}

	
	private class DownloadRequest implements Runnable {

		private URI uri;
		private String message_received = "not received yet";

		public DownloadRequest(String spec) {
//			try {
//				URI uri = new URI(spec);
//			} catch (URISyntaxException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			message_received = spec;
			System.out.println(message_received);
			
		}

		@Override
		public void run() {
			System.out.println(message_received);

		}

	}

	

}
