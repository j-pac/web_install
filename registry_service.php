<?php

require_once 'Publisher.php';

define("SERVICE_NAME", "web_install_");

if(isset($_REQUEST['username']) && isset($_REQUEST['device_imei']))
{
	// DATABASE AND RABBITMQ CONNECTIONS
	$db = pg_connect("host=localhost port=5432 dbname=register user=postgres password=godIsAProgrammer") or die('Connection to database failed: ' . pg_last_error());

	$publisher = new Publisher();

	$username = $_REQUEST['username'];
	$d_imei = $_REQUEST['device_imei'];
	// XMl is the default response format
	$format  = strtolower($_REQUEST['format']) == 'json' ? 'json' : 'xml'; 

	$queue_name = generateQueueName($username, $d_imei);

	// PREPARED STATEMENT AND INSERTION
	$p_statement = "INSERT INTO queue (device_imei, username, queue_name) VALUES ($1, $2, $3)";
	pg_prepare('register_query', $p_statement);
	pg_execute('register_query', array($d_imei, $username, $queue_name)) or die('Error while inserting!');

	// CREATING RABBITMQ QUEUE
	$publisher->create_queue($queue_name);


	$publisher->close();
	pg_close($db);

	if($format == 'json')
	  {
	    sendJSONResponse($queue_name);
	  }
	else
	  {
	    sendXMLResponse($queue_name);
	  }

}

function generateQueueName($username, $device_imei)
{
	$hash_input = $username . $device_imei;
	return SERVICE_NAME . sha1($hash_input);
}

function sendXMLResponse()
{
  header('Content-type: application/json');
  echo json_encode('Response'

}

function sendJSONResponse()
{

}


?>
