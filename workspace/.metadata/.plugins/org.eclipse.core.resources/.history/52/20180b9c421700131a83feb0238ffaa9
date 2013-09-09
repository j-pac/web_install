package cm.aptoide.pt.services;

import java.io.IOException;

import cm.aptoide.pt.Configs;
import cm.aptoide.pt.IntentReceiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class AsynchronousWebInstallService extends IntentService {

	private static final String TAG = "cm.aptoid.pt.services.AsynchronousWebInstallService";
	private static final long ELAPSED_TIME_TO_START = (long) 1 * 60 * 1000; // Starts
																			// each
																			// 5
																			// minutes
	private String rabbitmq_queue_id;

	private static AlarmManager alarm_manager = null;
	private static PendingIntent pending_intent = null;

	public static void turnOffAlarmManager() {
		if (alarm_manager != null) {
			alarm_manager.cancel(pending_intent);
		}
		alarm_manager = null;
	}

	public AsynchronousWebInstallService() {
		super("AsynchronousWebInstallService");

	}

	@Override
	public void onCreate() {

		super.onCreate();

		SharedPreferences sPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		rabbitmq_queue_id = sPref.getString(Configs.RABBITMQ_QUEUE_ID, null);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "AsynchronousWebInstallService Started");

		Toast.makeText(this, "AsynchronousWebInstallService onHandleIntent()",
				Toast.LENGTH_SHORT).show();

		if (rabbitmq_queue_id != null && isNetworkConnected(this)) {
			// Do the work -- check if rabbitmq has messages
			CheckRabbitMqClient();
			// Schedulling a new start
			scheduleNextBeggining();
		} else {
			turnOffAlarmManager();
		}
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "AsynchronousWebInstallService onDestroy()",
				Toast.LENGTH_SHORT).show();
		Log.i(TAG, "AsynchronousWebInstallService Stopped");

	}

	private void CheckRabbitMqClient() {
		AsynchronousRabbitMqClient rabbitmq_client = new AsynchronousRabbitMqClient(
				rabbitmq_queue_id);
		try {
			rabbitmq_client.establishConnection();
			String message_received = null;

			while ((message_received = rabbitmq_client.listenQueue()) != null) {
				downloadApk(message_received);
			}
		} finally {
			rabbitmq_client.closeConnection();
		}
	}

	private void downloadApk(String message) {
		Intent download_intent = new Intent(this, IntentReceiver.class);
		download_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		download_intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

		download_intent.putExtra("WebInstallRequest", message);
		startActivity(download_intent);

	}

	private void scheduleNextBeggining() {
		Intent service_launcher = new Intent(this,
				AsynchronousWebInstallService.class);

		pending_intent = PendingIntent.getService(this, 0, service_launcher,
				PendingIntent.FLAG_UPDATE_CURRENT);

		alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		long next_run_time = System.currentTimeMillis() + ELAPSED_TIME_TO_START;

		alarm_manager.set(AlarmManager.RTC, next_run_time, pending_intent);
	}

	private boolean isNetworkConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.getState() == NetworkInfo.State.CONNECTED;
	}

	private class AsynchronousRabbitMqClient {

		private com.rabbitmq.client.Connection connection;
		private com.rabbitmq.client.Channel channel;

		private String queue_routing_key;

		public AsynchronousRabbitMqClient(String queue_routing_key) {
			this.queue_routing_key = queue_routing_key;
		}

		public void establishConnection() {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Configs.LOCAL_IP);
			try {
				connection = factory.newConnection();
				channel = connection.createChannel();

				// channel.queueDeclare(queue_routing_key, true, false, false,
				// null);

				channel.basicQos(0);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public String listenQueue() {

			String message = null;

			boolean autoAck = false;
			GetResponse response;
			try {
				response = channel.basicGet(queue_routing_key, autoAck);

				if (response != null) {
					AMQP.BasicProperties props = response.getProps();
					long deliveryTag = response.getEnvelope().getDeliveryTag();
					message = new String(response.getBody(), "UTF-8");
					channel.basicAck(deliveryTag, false);

					return message;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public void closeConnection() {
			try {
				if (connection.isOpen() && channel != null
						&& connection != null) {
					channel.close();
					connection.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
