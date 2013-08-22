package com.aptoide.androidclient;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {

	public enum ServiceRunningState {
		SYNCHRONOUS, ASYNCHRONOUS;
	}

	private boolean isRunning = false;
	private ServiceRunningState running_state = ServiceRunningState.SYNCHRONOUS;
	
	@Override
	public void onCreate() {
		isRunning = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		//if()
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	public class AsynchronousSomething extends IntentService {

		public AsynchronousSomething() {
			super("name");
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onHandleIntent(Intent intent) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
