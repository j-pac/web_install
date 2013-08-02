<?php

require_once 'Publisher.php';

if(isset($_REQUEST['username']) && isset($_REQUEST['device_imei']))
{
	// DATABASE AND RABBITMQ CONNECTIONS
	$db = pg_connect("host=localhost port=5432 dbname=register user=postgres password=blfa5661") or die('Connection to database failed: ' . pg_last_error());

	$publisher = new Publisher();

	$username = $_REQUEST['username'];
	$d_imei = $_REQUEST['device_imei'];

	$queue_name = generateQueueName($username, $d_imei);

	// PREPARED STATEMENT AND INSERTION
	$p_statement = "INSERT INTO queue (device_imei, username, queue_name) VALUES ($1, $2, $3)";
	pg_prepare('register_query', $p_statement);
	pg_execute('register_query', array($d_imei, $username, $queue_name)) or die('Error while inserting!');

	// CREATING RABBITMQ QUEUE
	$publisher->create_queue($queue_name);


	$publisher->close();
	pg_close($db);

	print "Registry service closed!";
}

function generateQueueName($username, $device_imei)
{
	$hash_input = $username . $device_imei;
	return sha1($hash_input);
}

?>
