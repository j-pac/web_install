<?php

require_once 'Publisher.php';

define("SERVICE_NAME", "web_install_");
define("LOG_FILE", "./registry_service_log.log");

if(isset($_REQUEST['token']) && isset($_REQUEST['device_id']) && isset($_REQUEST['device_model']) && isset($_REQUEST['mode']))
{

        // Request Parameters escaped to avoid SQL injection
        $token = pg_escape_string(utf8_encode($_REQUEST['token']));
        $device_id = pg_escape_string(utf8_encode($_REQUEST['device_id']));
        $device_model = pg_escape_string(utf8_encode($_REQUEST['device_model']));
        $mode  = strtolower($_REQUEST['mode']) == 'json' ? 'json' : 'xml';

			
        // Database Connection
        $db = pg_connect("host=localhost port=5432 dbname=aptoide user=postgres password=godIsAProgrammer");
	if(!$db)
	{
	  $r_body = createResponse('FAIL', '', 'Internal Server Error'); 
	  $r_body = formatResponse($r_body, $mode);
	  sendResponse(500, 'Internal Server Error', $r_body, 'application/' . $mode);
	  exit();
	}
	
        $queue_id = generateQueueName($token, $device_id);

        // Register device in Aptoide Database, if its a valid aptoide user token and device its not registered yet
	$register_query = pg_query($db, "INSERT INTO device (device_id, model, registered_on) SELECT '{$device_id}', '{$device_model}', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM device WHERE device_id = '{$device_id}') AND EXISTS (SELECT 1 FROM aptoide_user WHERE token = '{$token}')");
	
	// Associate device with user aptoide account
	$association_query = pg_query($db, "INSERT INTO user_device (device_id, usertoken, queue_id, association_date) SELECT '{$device_id}', '{$token}', '{$queue_id}', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM user_device WHERE device_id = '{$device_id}' AND usertoken = '{$token}')");

	pg_close($db);
	
	if(!$association_query) 
	{
	  $response = createResponse('FAIL', '', 'Unauthorized');
	  $response = formatResponse($response, $mode);
	  sendResponse(401, 'Unauthorized', $response, 'application/' . $mode);
	  exit();
	}

	// Create RabbitMq user queue
	$publisher = new Publisher();
	
	$publisher->create_queue($queue_id);

	$publisher->close();
	
	$response = createResponse('OK', $queue_id, '');
	$response = formatResponse($response, $mode);
	sendResponse(200, 'OK', $response, 'application/'. $mode);
}
else
{
  sendResponse(400, 'Bad Request', 'Invalid Request');
}


function generateQueueName($token, $device_id)
{
	$hash_input = $token . $device_id;
	return SERVICE_NAME . sha1($hash_input);
}

function createResponse($status, $queue_id, $error) {
  $response = array();
  $response['status'] = $status;
  $response['queue_id'] = $queue_id;
  $response['errors'] = $error;
  return $response;
}

function formatResponse($response, $mode) {
  if($mode == 'json') 
  {
    return json_encode($response);
  }
  else 
  {
    $xml = new SimpleXMLElement('<?xml version="1.0" encoding="UTF-8"?><Response />');
    foreach($response as $key => $value) 
    {
	$xml->addChild($key, $value);
    }
    return $xml->asXML();
  }
}

function sendResponse($status_code = 200, $status_message = 'OK', $body = '', $content_type = 'text/html')
{
  $status_header ='HTTP/1.1 ' . $status_code . ' ' . $status_message;
  header($status_header);
  header('Content-type: ' . $content_type);
  echo $body;
}

?>
