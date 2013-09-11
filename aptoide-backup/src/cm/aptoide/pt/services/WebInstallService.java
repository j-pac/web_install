package cm.aptoide.pt.services;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cm.aptoide.pt.Configs;
import cm.aptoide.pt.IntentReceiver;
import cm.aptoide.pt.util.Algorithms;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.QueueingConsumer.Delivery;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class WebInstallService extends Service {

	private static final String TAG = "cm.aptoide.pt.services.WebInstallService";

	private Context context;

	private RabbitMqClient rabbitmq_client;

	private String rabbitmq_queue_id;

	private Thread rabbitMq_pull_thread;

	private static boolean isRunning = false;

	public static boolean isRunning() {
		return isRunning;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "WebInstallService created!");
		context = this;
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

		rabbitMq_pull_thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					rabbitmq_client = new RabbitMqClient(rabbitmq_queue_id);
					rabbitmq_client.establishConnection();

					while (isRunning) {
						final String myapp_received;

						myapp_received = rabbitmq_client.waitForMessage();

						String private_key = PreferenceManager
								.getDefaultSharedPreferences(context)
								.getString(Configs.LOGIN_USER_TOKEN, null);

						if (checkMessageAuthenticity(myapp_received,
								private_key)) {
							Log.i(TAG,
									"Received WebInstall Request Authenticated");
							processInstallRequest(myapp_received);
						} else {
							Log.w(TAG,
									"X-->Received WebInstall Request Not Authenticated");
						}

					}

				} catch (ShutdownSignalException e) {
					e.printStackTrace();
				} catch (ConsumerCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch bloc
					e.printStackTrace();
				}
			}
		});

		rabbitMq_pull_thread.start();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		Toast.makeText(getApplicationContext(),
				"WebInstallService Stopped - onDestroy()! :)",
				Toast.LENGTH_SHORT).show();

		isRunning = false;

		rabbitmq_client.closeConnection();

		Log.i(TAG, "WebInstallService destroyed!");

	}

	private void processInstallRequest(String myapp) {
		Intent download_intent = new Intent(this, IntentReceiver.class);
		download_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		download_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		download_intent.putExtra("WebInstallRequest", myapp);
		startActivity(download_intent);
	}

	@SuppressLint("NewApi")
	public static boolean checkMessageAuthenticity(String xml_message,
			String private_key) {

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml_message));

			Document doc = db.parse(is);

			Element hmac = (Element) doc.getElementsByTagName("hmac").item(0);
			String hmac_sign = hmac.getTextContent();
			hmac.getParentNode().removeChild(hmac);

			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			String message_str = writer.toString();

			String hmac_result = Algorithms.computeHmacSha1(message_str,
					private_key);

			Log.i(TAG, "Myapp hmac signature: \n" + hmac_sign);
			Log.i(TAG, "Hmac Algorithm result: \n" + hmac_result);

			return hmac != null && private_key != null
					&& (hmac_sign.equals(hmac_result));

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

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

		public String waitForMessage() throws ShutdownSignalException,
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
				if (connection != null && connection.isOpen()) {
					connection.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
