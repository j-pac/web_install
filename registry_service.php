<?php

require_once 'Publisher.php';

define("SERVICE_NAME", "web_install_");

if(isset($_REQUEST['token']) && isset($_REQUEST['device_id']) && isset($_REQUEST['mode']))
{

        // Request Parameters
        $token = $_REQUEST['token'];
        $device_id = $_REQUEST['device_id'];
        // XMl is the default response format                                                            
        $mode  = strtolower($_REQUEST['mode']) == 'json' ? 'json' : 'xml';

	//Response Parameters
	$status;
	$error= '';
	
        // DATABASE AND RABBITMQ CONNECTIONS
        $db = pg_connect("host=localhost port=5432 dbname=register user=postgres password=godIsAProgrammer") or die('Connection to database failed: ' . pg_last_error());

        $publisher = new Publisher();

        $queue_id = generateQueueName($username, $device_id);

        // PREPARED STATEMENT AND INSERTION
	$p_statement = "INSERT INTO queue (device_imei, username, queue_name) VALUES ($1, $2, $3)";
	pg_prepare('register_query', $p_statement);
	pg_execute('register_query', array($device_id, $token, $queue_id)) or die('Error while inserting!');

	// CREATING RABBITMQ QUEUE
	$publisher->create_queue($queue_id);

	$publisher->close();
	pg_close($db);

	$status = 'OK';

	$response = array();
	$response['status'] = $status;
	$response['queue_id'] = $queue_id;
	$response['errors'] = $error;
	
	if($mode == 'json')
	  {
	    sendResponse(200, 'OK', json_encode($response), 'application/json');
	  }
	else
	  {
	    $xml = new SimpleXMLElement('<?xml version="1.0" encoding="UTF-8"?><Response />');
	    foreach($response as $key => $value) 
	    {
	      $xml->addChild($key, $value);
	    }    
	    sendResponse(200, 'OK', $xml->asXML(), 'text/xml');
	  }
}
else
{
  sendResponse(400, 'Bad Request', 'Invalid Request');
}


function generateQueueName($username, $device_imei)
{
	$hash_input = $username . $device_imei;
	return SERVICE_NAME . sha1($hash_input);
}


function sendResponse($status_code = 200, $status_message = 'OK', $body = '', $content_type = 'text/html')
{
  $status_header ='HTTP/1.1 ' . $status_code . ' ' . $status_message;
  header($status_header);
  header('Content-type: ' . $content_type);
  echo $body;
}

?>
