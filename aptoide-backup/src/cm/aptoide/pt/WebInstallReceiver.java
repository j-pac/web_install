package cm.aptoide.pt;

import cm.aptoide.pt.services.AsynchronousWebInstallService;
import cm.aptoide.pt.services.RealWebInstallService;
import cm.aptoide.pt.services.WebInstallService;
import cm.aptoide.pt.webservices.login.Login;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class WebInstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			if (Login.isDeviceRegistered(context)) {
				Intent i = new Intent(context,
						AsynchronousWebInstallService.class);
				context.startService(i);
			}
		} else if (action.equals("cm.aptoide.pt.SYNC_START")) {
			// stops the async service if running
			AsynchronousWebInstallService.turnOffAlarmManager();
			// starts the sync service
			Intent i = new Intent(context, WebInstallService.class);
			context.startService(i);
		} else if (action.equals("cm.aptoide.pt.SYNC_STOP")) {
			// stop the sync service
			Intent i = new Intent(context, WebInstallService.class);
			context.stopService(i);
			// start the async service
			if (Login.isDeviceRegistered(context)) {
				Intent i2 = new Intent(context,
						AsynchronousWebInstallService.class);
				context.startService(i2);
			}

		}
	}
}
