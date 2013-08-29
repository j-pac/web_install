package cm.aptoide.pt.services;

import java.io.IOException;

import cm.aptoide.pt.IntentReceiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AsynchronousWebInstallService extends IntentService {

	private static final String TAG = "cm.aptoid.pt.services.AsynchronousWebInstallService";
	private static final long ELAPSED_TIME_TO_START = 5*60*1000; // Starts each 5
																// minutes,
																// while device
	// is associated
	// with aptoide

	private final String queue_routing_key = "web_install_4d4e2d90c4d6eec405d82ecc795a7e3592df1523";

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
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "AsynchronousWebInstallService Started");

		Toast.makeText(this, "AsynchronousWebInstallService onHandleIntent()",
				Toast.LENGTH_SHORT).show();

		// Do the work -- check if rabbitmq has messages
		CheckRabbitMqClient();
		// Schedulling a new start
		scheduleNextBeggining();

	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "AsynchronousWebInstallService onDestroy()",
				Toast.LENGTH_SHORT).show();
	}

	private void CheckRabbitMqClient() {
		AsynchronousRabbitMqClient rabbitmq_client = new AsynchronousRabbitMqClient(
				queue_routing_key);
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

	private class AsynchronousRabbitMqClient {

		private com.rabbitmq.client.Connection connection;
		private com.rabbitmq.client.Channel channel;

		private String queue_routing_key;

		public AsynchronousRabbitMqClient(String queue_routing_key) {
			this.queue_routing_key = queue_routing_key;
		}

		public void establishConnection() {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("10.0.2.2");
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
				channel.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
