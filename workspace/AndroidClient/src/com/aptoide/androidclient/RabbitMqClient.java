package com.aptoide.androidclient;

import java.io.IOException;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMqClient {

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

			channel.queueDeclare(queue_routing_key, true, false, false, null);

			channel.basicQos(0);

			consumer = new QueueingConsumer(channel);
			channel.basicConsume(queue_routing_key, false, consumer);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String consumeMessage() {

		String message = null;
		Delivery delivery;

		try {
			delivery = consumer.nextDelivery();

			message = new String(delivery.getBody());

			/*
			 * runOnUiThread(new Runnable() { public void run() {
			 * Toast.makeText(RabbitMqClient.this, message, Toast.LENGTH_LONG)
			 * .show(); } });
			 */

			// PARSE XML

			// CALL SOMETHING TO DOWNLOAD AND INSTALL THE APK

			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

			System.out.println("Message obtained: " + message);

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

}
