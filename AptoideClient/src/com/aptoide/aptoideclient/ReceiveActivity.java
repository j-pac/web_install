package com.aptoide.aptoideclient;

import java.io.IOException;

import com.rabbitmq.client.AMQP.Channel;
import com.rabbitmq.client.AMQP.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class ReceiveActivity extends Activity {

	private static final String DEVICE_ID = "ABC";

	private com.rabbitmq.client.Connection connection;
	private com.rabbitmq.client.Channel channel;
	private QueueingConsumer consumer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);

		
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				establishConnection();
				
				consumeMessages();
				
			}
		}).start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive, menu);
		return true;
	}

	private void establishConnection() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("10.0.2.2");
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(DEVICE_ID, true, false, false, null);

			channel.basicQos(0);

			consumer = new QueueingConsumer(channel);
			channel.basicConsume(DEVICE_ID, false, consumer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
	}

	private void consumeMessages() {
		while (true) {
			QueueingConsumer.Delivery delivery;

			try {
				delivery = consumer.nextDelivery();

				final String message = new String(delivery.getBody());

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ReceiveActivity.this, message, Toast.LENGTH_LONG)
						.show();
					}
				});
				

				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			} catch (ShutdownSignalException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ConsumerCancelledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
