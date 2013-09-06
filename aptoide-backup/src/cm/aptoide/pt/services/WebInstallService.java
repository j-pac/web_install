package cm.aptoide.pt.services;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import cm.aptoide.pt.Configs;
import cm.aptoide.pt.IntentReceiver;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class WebInstallService extends Service {

	private static final String TAG = "cm.aptoide.pt.services";

	private RabbitMqClient rabbitmq_client;

	private String rabbitmq_queue_id;

	private Runnable rabbitMq_pull_task;

	private ExecutorService executor = Executors.newSingleThreadExecutor();

	private static boolean isRunning = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "WebInstallService created!");
		SharedPreferences sPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		rabbitmq_queue_id = sPref.getString(Configs.RABBITMQ_QUEUE_ID, null);
		if (rabbitmq_queue_id != null) {
			isRunning = true;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "WebInstallService running!");

		Toast.makeText(this,
				"WebInstallService Started - onStartCommand()! :)",
				Toast.LENGTH_SHORT).show();

		rabbitMq_pull_task = new Runnable() {

			@Override
			public void run() {
				try {
					rabbitmq_client = new RabbitMqClient(rabbitmq_queue_id);
					rabbitmq_client.establishConnection();

					while (isRunning) {
						String message_received;

						message_received = rabbitmq_client.listenQueue();

						downloadApk(message_received);
					}

				} catch (ShutdownSignalException e) {
					Toast.makeText(getApplicationContext(),
							"Received shutdown signal!!!", Toast.LENGTH_LONG)
							.show();

					e.printStackTrace();
				} catch (ConsumerCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {

					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(),
						"MyService Thread ended!!!", Toast.LENGTH_LONG).show();
			}
		};

		executor.submit(rabbitMq_pull_task);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		Toast.makeText(getApplicationContext(),
				"WebInstallService Stopped - onDestroy()! :)",
				Toast.LENGTH_SHORT).show();

		isRunning = false;

		executor.shutdownNow();
		rabbitmq_client.closeConnection();

		Log.i(TAG, "WebInstallService destroyed!");

	}

	private void downloadApk(String message) {
		Intent download_intent = new Intent(this, IntentReceiver.class);
		download_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		download_intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

		download_intent.putExtra("WebInstallRequest", message);
		startActivity(download_intent);

	}

	private class RabbitMqClient {

		private com.rabbitmq.client.Connection connection;
		private com.rabbitmq.client.Channel channel;
		private com.rabbitmq.client.Consumer consumer;

		private String queue_routing_key;

		public RabbitMqClient(String queue_routing_key) {
			this.queue_routing_key = queue_routing_key;
		}

		public void establishConnection() {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Configs.LOCAL_IP);
			factory.setConnectionTimeout(0);
			// factory.setRequestedHeartbeat(10);

			try {
				connection = factory.newConnection();
				connection.addShutdownListener(new ShutdownListener() {

					@Override
					public void shutdownCompleted(ShutdownSignalException arg0) {
						stopSelf();

					}
				});

				channel = connection.createChannel();

				// queueDeclare(java.lang.String queue,
				// boolean passive,
				// boolean durable,
				// boolean exclusive,
				// boolean autoDelete,
				channel.queueDeclare(queue_routing_key, true, false, false,
						null);

				channel.basicQos(0);

				consumer = new QueueingConsumer(channel);

				channel.basicConsume(queue_routing_key, false, consumer);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public String listenQueue() throws ShutdownSignalException,
				ConsumerCancelledException, InterruptedException, IOException {

			String message = null;
			Delivery delivery;

			delivery = ((QueueingConsumer) consumer).nextDelivery();

			message = new String(delivery.getBody());

			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			return message;
		}

		public void closeConnection() {
			try {
				if (connection.isOpen() && connection != null) {
					connection.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isRunning() {
		return isRunning;
	}

}
