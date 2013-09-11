<?php

require_once('/home/brutus/lib/php-amqplib/amqp.inc');

class Publisher {

      	private $connection;
	private $channel;

	public function Publisher() {

	        //Establish connection with RabbitMq
		$this->connection = new AMQPConnection('localhost', 5672, 'guest', 'guest');
		$this->channel = $this->connection->channel();
	}

	// Create a "durable" Queue
	public function create_queue($queue_name) {
		$this->channel->queue_declare($queue_name, false, true, false, false);
	}

	// Publish a "persistent" message
	public function publish_message($data, $queue_name) {
	  $message = new AMQPMessage($data, array('delivery_mode' => 2));
		$this->channel->basic_publish($message, '', $queue_name);
	}

	public function close() {
	       $this->channel->close();
	       $this->connection->close();
	 }
}
?>