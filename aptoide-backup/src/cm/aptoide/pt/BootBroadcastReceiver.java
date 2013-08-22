package cm.aptoide.pt;

import cm.aptoide.pt.services.AsynchronousWebInstallService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = ".services.AsynchronousWebInstallService";

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent asyncWebInstall_intent = new Intent(context,
				AsynchronousWebInstallService.class);
		context.startService(asyncWebInstall_intent);
		Log.i(TAG, "AsynchronousWebInstallService launched");
	}

}
