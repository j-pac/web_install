package cm.aptoide.pt;

import cm.aptoide.pt.services.AsynchronousWebInstallService;
import cm.aptoide.pt.services.WebInstallService;
import cm.aptoide.pt.webservices.login.Login;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class WebInstallReceiver extends BroadcastReceiver {

	private static boolean isSyncRunningTime = false;
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String action = intent.getAction();
		boolean isNetworkConnected = checkNetworkConnection(context);

		if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			if (Login.isDeviceRegistered(context)) {
				// When the connection is lost
				if (!isNetworkConnected) {
					if (isSyncRunningTime) {
						stopSynchronousService();
					} else {
						stopAsynchronousService();
					}
				} else {
					// When connection is recovered
					if (isSyncRunningTime) {
						startSynchronousService();
						Toast.makeText(context, "LIGAR SERVICO SINCRONO",
								Toast.LENGTH_SHORT).show();
					} else {
						startAsynchronousService();
						Toast.makeText(context, "LIGAR SERVICO ASSINCRONO",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			if (Login.isDeviceRegistered(context) && isNetworkConnected) {
				startAsynchronousService();
			}
		} else if (action.equals("cm.aptoide.pt.SYNC_START")) {
			Toast.makeText(context, "SYNC_START CALLED!!!", Toast.LENGTH_SHORT)
					.show();
			// stops the async service if running
			stopAsynchronousService();
			if (isNetworkConnected) {
				startSynchronousService();
			}
			isSyncRunningTime = true;
		} else if (action.equals("cm.aptoide.pt.SYNC_STOP")) {
			Toast.makeText(context, "SYNC_STOP CALLED!!!", Toast.LENGTH_SHORT)
					.show();
			if (WebInstallService.isRunning()) {
				stopSynchronousService();
			}
			isSyncRunningTime = false;
			if (Login.isDeviceRegistered(context) && isNetworkConnected) {
				startAsynchronousService();
			}
		}
	}

	private void startSynchronousService() {
		Intent i = new Intent(context, WebInstallService.class);
		context.startService(i);
	}

	private void stopSynchronousService() {
		Intent i = new Intent(context, WebInstallService.class);
		context.stopService(i);
	}

	private void startAsynchronousService() {
		Intent i = new Intent(context, AsynchronousWebInstallService.class);
		context.startService(i);
	}

	private void stopAsynchronousService() {
		AsynchronousWebInstallService.turnOffAlarmManager();
	}

	private boolean checkNetworkConnection(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork.isConnected());
	}
}
