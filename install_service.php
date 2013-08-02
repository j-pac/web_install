<?php

require_once 'Publisher.php';


	$username = $_REQUEST['username'];
	$d_imei = $_REQUEST['device_imei'];
	$message = $_REQUEST['message'];

	// ESTABLISH CONNECTIONS WITH POSTGRES AND RABBITMQ
	$db = pg_connect("host=localhost port=5432 dbname=register user=postgres password=blfa5661") or die("could not connect");

	$publisher = new Publisher();

	// QUERYNG
	$pstm = "SELECT queue_name FROM queue WHERE device_imei=$1 AND username=$2";
	pg_prepare('install_query', $pstm);

	$result = pg_execute('install_query', array($d_imei, $username));
	$queue_name = pg_fetch_result($result, 0, 0);

	
	// PUBLISHING
	$publisher->publish_message($message, $queue_name);

	$publisher->close();
	pg_close($db);

	print "Install service closed!";

?>
