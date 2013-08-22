package cm.aptoide.pt.services;

import java.io.IOException;


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
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RealWebInstallService extends Service {

	private enum ServiceMode {
		SYNCHRONOUS, ASYNCHRONOUS;

		public static ServiceMode getSynchronousMode() {
			return SYNCHRONOUS;
		}

		public static ServiceMode getAsynchronousMode() {
			return ASYNCHRONOUS;
		}

		public static boolean isSynchronousMode(ServiceMode service_state) {
			return service_state == SYNCHRONOUS;
		}
	}

	private static final String TAG = "cm.aptoide.pt.services.WebInstallService";

	private final String queue_routing_key = "web_install_4d4e2d90c4d6eec405d82ecc795a7e3592df1523";

	private ServiceMode service_mode;

	private RabbitMqClient rabbitmq_client;

	private Thread rabbitMq_pull_thread;

	
	public void runInSynchronousMode() {
		service_mode = ServiceMode.getSynchronousMode();
	}
	
	public void runInAsynchronousMode() {
		service_mode = ServiceMode.getAsynchronousMode();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(
				this,
				service_mode.toString() + " WebInstallService onStartCommand()",
				Toast.LENGTH_LONG).show();
		Log.i(TAG,
				"WebInstallService started running in "
						+ service_mode.toString() + " mode!");

		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class BlockingPullThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
		}
	}

	private class PollingThread extends Thread {

		public PollingThread() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void run() {
			
			if (service_is_needed) {
				// Do the work -- check if rabbitmq has messages
				CheckRabbitMqClient();
				// Schedulling a new start
				scheduleNextBeggining();
			}
		}
		
		private void CheckRabbitMqClient() {
			RabbitMqClient rabbitmq_client = new RabbitMqClient(
					queue_routing_key);
			try {
				rabbitmq_client.establishConnection();
				String message_received = null;

				while ((message_received = rabbitmq_client.listenQueue()) != null) {
					System.out.println("Message received asynchronously: "
							+ message_received);
					// sendbroadcast to intentReceiver to manage the download
				}
			} finally {
				rabbitmq_client.closeConnection();
			}
		}

		private void scheduleNextBeggining() {
			Intent service_launcher = new Intent(this,
					AsynchronousWebInstallService.class);

			PendingIntent pending_intent = PendingIntent.getService(this, 0,
					service_launcher, PendingIntent.FLAG_UPDATE_CURRENT);

			AlarmManager alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

			long next_run_time = System.currentTimeMillis() + ELAPSED_TIME_TO_START;
			
			alarm_manager.set(AlarmManager.RTC, next_run_time,
					pending_intent);
		}
	}

	private class RabbitMqClient {

		private com.rabbitmq.client.Connection connection;
		private com.rabbitmq.client.Channel channel;
		private com.rabbitmq.client.QueueingConsumer consumer;

		private String queue_routing_key;

		public RabbitMqClient(String queue_routing_key) {
			this.queue_routing_key = queue_routing_key;
		}

		public void startConnection() {

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

		public void startConnectionImplementingConsumer() {
			startConnection();

			consumer = new QueueingConsumer(channel);
			try {
				channel.basicConsume(queue_routing_key, false, consumer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String waitForMessage() {

			String message = null;
			Delivery delivery;

			try {
				delivery = consumer.nextDelivery();

				message = new String(delivery.getBody());

				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

				// System.out.println("Message obtained: " + message);

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

		public String checkForMessage() {

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
