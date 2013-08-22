package cm.aptoide.pt.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cm.aptoide.pt.IntentReceiver;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class WebInstallService extends Service {

	private static final String TAG = "cm.aptoide.pt.services";

	private final String queue_routing_key = "web_install_4d4e2d90c4d6eec405d82ecc795a7e3592df1523";

	private RabbitMqClient rabbitmq_client;

	private Thread rabbitMq_pull_thread;

	private static boolean isRunning = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Service RabbitMqHandlerService created!");

		// testing purposes
		if (isRunning) {
			Toast.makeText(
					getApplicationContext(),
					"ERROR -- THE WEB INSTALL SERVICE HAS STARTED WHILE ITS ALREADY RUNNING!!!",
					Toast.LENGTH_LONG).show();
		}

		isRunning = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service RabbitMqHandlerService onStartCommand!");

		Toast.makeText(this,
				"WebInstallService Started - onStartCommand()! :)",
				Toast.LENGTH_LONG).show();

		rabbitMq_pull_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					rabbitmq_client = new RabbitMqClient(queue_routing_key);
					rabbitmq_client.establishConnection();

					while (isRunning) {
						String message_received = rabbitmq_client
								.listenQueue();
						System.out.println("message received on hadler thread: " + message_received);

						new Thread(new DownloadRequest(message_received)).start();

						// Toast.makeText(getApplicationContext(),
						// "message received: " + message_received,
						// Toast.LENGTH_SHORT).show();

					}
					
				} finally {
					rabbitmq_client.closeConnection();
				}

			}
		});

		rabbitMq_pull_thread.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service RabbitMqHandlerService destroyed!");

		Toast.makeText(this, "WebInstallService Stopped - onDestroy()! :)",
				Toast.LENGTH_LONG).show();
		
		isRunning = false;
		rabbitMq_pull_thread.interrupt();
	}

	
	private class DownloadRequest implements Runnable {

		private String uri;

		public DownloadRequest(String spec) {
			uri = spec;
		}

		@Override
		public void run() {
			System.out.println("Message received from broker: " + uri);
			Intent i = new Intent(getApplicationContext(), IntentReceiver.class);
			i.putExtra("WebInstallRequest", uri);
		}

	}

	public static boolean isRunning() {
		return isRunning;
	}

	private class RabbitMqClient {

		private com.rabbitmq.client.Connection connection;
		private com.rabbitmq.client.Channel channel;
		private com.rabbitmq.client.QueueingConsumer consumer;

		private String queue_routing_key;

		public RabbitMqClient(String queue_routing_key) {
			this.queue_routing_key = queue_routing_key;
		}

		public void establishConnection() {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("10.0.2.2");
			try {
				connection = factory.newConnection();
				channel = connection.createChannel();

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

		public String listenQueue() {

			String message = null;
			Delivery delivery;

			try {
				delivery = consumer.nextDelivery();

				message = new String(delivery.getBody());

				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

//				System.out.println("Message obtained: " + message);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ShutdownSignalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConsumerCancelledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return message;
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
