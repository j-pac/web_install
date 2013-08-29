package cm.aptoide.pt.services;

import java.io.IOException;

import cm.aptoide.pt.IntentReceiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RealWebInstallService extends Service {

	public enum ServiceRunningState {
		SYNCHRONOUS, ASYNCHRONOUS, STOPPED;

		public static ServiceRunningState getSynchronousMode() {
			return SYNCHRONOUS;
		}

		public static ServiceRunningState getAsynchronousMode() {
			return ASYNCHRONOUS;
		}

		public static ServiceRunningState getStoppedState() {
			return STOPPED;
		}

		public static boolean isSynchronousMode(
				ServiceRunningState service_state) {
			return service_state == SYNCHRONOUS;
		}

		public static boolean isAsynchronousMode(
				ServiceRunningState service_state) {
			return service_state == ASYNCHRONOUS;
		}

		public static boolean isStopped(ServiceRunningState service_state) {
			return service_state == STOPPED;
		}
	}

	private static final String TAG = "cm.aptoide.pt.services.WebInstallService";

	private static final long ELAPSED_TIME_TO_START = 15000;

	private final String queue_routing_key = "web_install_4d4e2d90c4d6eec405d82ecc795a7e3592df1523";

	private SharedPreferences Spref;

	private static ServiceRunningState service_state;

	private RabbitMqClient rabbitmq_client;

	private boolean isRunning = false;

	private static PendingIntent service_pending_intent = null;
	private static AlarmManager alarm_manager = null;

	private Thread serviceWorkerThread;

	public static void runInSynchronousMode() {
		if (alarm_manager != null) {
			alarm_manager.cancel(service_pending_intent);
		}
		alarm_manager = null;
		service_pending_intent = null;
		service_state = ServiceRunningState.getSynchronousMode();
	}

	public static void runInAsynchronousMode() {
		service_state = ServiceRunningState.getAsynchronousMode();
	}

	public static void stopService() {
		service_state = ServiceRunningState.STOPPED;
	}

	public static ServiceRunningState getCurrentServiceState() {
		return service_state;
	}
	
	public static boolean isRunning() {
		return service_state != ServiceRunningState.getStoppedState();
	}

	private void sendWebInstallIntent(String message) {
		Intent download_intent = new Intent(this, IntentReceiver.class);
		download_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		download_intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

		download_intent.putExtra("WebInstallRequest", message);
		startActivity(download_intent);

	}

	private void checkForMessages() {

		try {
			rabbitmq_client.connect();

			String message_received = null;

			while ((message_received = rabbitmq_client.checkForMessage()) != null) {
				System.out.println("Message received asynchronously: "
						+ message_received);
				Toast.makeText(this, "Message received: " + message_received,
						Toast.LENGTH_SHORT).show();

				sendWebInstallIntent(message_received);
			}
		} catch (IOException e) {
			System.out.println("WebInstallService : checkForMessages() - "
					+ e.getMessage());
		} finally {
			rabbitmq_client.close();
		}
	}

	private void scheduleNextBeggining() {
		Intent service_launcher = new Intent(this, RealWebInstallService.class);

		service_pending_intent = PendingIntent.getService(this, 0,
				service_launcher, PendingIntent.FLAG_UPDATE_CURRENT);

		alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		long next_run_time = System.currentTimeMillis() + ELAPSED_TIME_TO_START;

		alarm_manager.set(AlarmManager.RTC, next_run_time, service_pending_intent);
		

	}

	private void waitForMessages() {
		try {
			rabbitmq_client.connectImplementingConsumer();

			while (!ServiceRunningState.isSynchronousMode(service_state)) {
				String message_received = rabbitmq_client.waitForMessage();

				sendWebInstallIntent(message_received);
			}

		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			rabbitmq_client.close();

		}
	}

	@Override
	public void onCreate() {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(
				this,
				service_state.toString()
						+ " WebInstallService onStartCommand()",
				Toast.LENGTH_LONG).show();
		Log.i(TAG,
				"WebInstallService started running in "
						+ service_state.toString() + " mode!");

		serviceWorkerThread = new Thread(new Runnable() {

			@Override
			public void run() {

				rabbitmq_client = new RabbitMqClient(queue_routing_key);

				while (!ServiceRunningState.isStopped(service_state)) {
					if (ServiceRunningState.isAsynchronousMode(service_state)) {
						// checkRAbbitMqforMessages
						checkForMessages();
						// scheduleNextBeggining
						scheduleNextBeggining();
						// stop the service
						stopSelf();

					} else {
						// waitInRabbitMqforMessages
						waitForMessages();
					}
				}
			}
		});

		serviceWorkerThread.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		service_state = ServiceRunningState.getStoppedState();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class RabbitMqClient {

		private com.rabbitmq.client.Connection connection;
		private com.rabbitmq.client.Channel channel;
		private com.rabbitmq.client.QueueingConsumer consumer;

		private String queue_routing_key;

		public RabbitMqClient(String queue_routing_key) {
			this.queue_routing_key = queue_routing_key;
		}

		public void connect() {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("10.0.2.2");
			try {
				connection = factory.newConnection();
				channel = connection.createChannel();

				channel.queueDeclare(queue_routing_key, true, false, false,
						null);

				channel.basicQos(0);

			} catch (IOException e) {
				System.err
						.println("IOEXception RabbitMqClient - startConnection(): "
								+ e.getMessage());
			}

		}

		// method created for asynchronous mode using a queue consumer
		public void connectImplementingConsumer() {
			connect();

			consumer = new QueueingConsumer(channel);
			try {
				channel.basicConsume(queue_routing_key, false, consumer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String waitForMessage() throws IOException,
				ShutdownSignalException, ConsumerCancelledException,
				InterruptedException {

			String message = null;
			Delivery delivery;

			delivery = consumer.nextDelivery();

			message = new String(delivery.getBody());

			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			return message;
		}

		public String checkForMessage() throws IOException {

			String message = null;

			boolean autoAck = false;
			GetResponse response;

			response = channel.basicGet(queue_routing_key, autoAck);

			if (response != null) {
				AMQP.BasicProperties props = response.getProps();
				long deliveryTag = response.getEnvelope().getDeliveryTag();
				message = new String(response.getBody(), "UTF-8");
				channel.basicAck(deliveryTag, false);
			}
			return message;

		}

		public void close() {
			try {
				channel.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
