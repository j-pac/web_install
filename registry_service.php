<?php

require_once 'Publisher.php';

define("SERVICE_NAME", "web_install_");
$log_file = file('~/registry_webservice_log.log');

if(isset($_REQUEST['token']) && isset($_REQUEST['device_id']) && isset($_REQUEST['device_model']) && isset($_REQUEST['mode']) && isset($_REQUEST['signature']))
{

        // Request Parameters escaped to avoid SQL injection
        $token = pg_escape_string(utf8_encode($_REQUEST['token']));
        $device_id = pg_escape_string(utf8_encode($_REQUEST['device_id']));
        $device_model = pg_escape_string(utf8_encode($_REQUEST['device_model']));
	$signature = $_REQUEST['signature'];
        $mode = strtolower($_REQUEST['mode']) == 'json' ? 'json' : 'xml';

			
        // Database Connection
        $db = pg_connect("host=localhost port=5432 dbname=aptoide user=postgres password=godIsAProgrammer");
	if(!$db)
	{
	  $r_body = createResponse('FAIL', '', 'Internal Server Error'); 
	  $r_body = formatResponse($r_body, $mode);
	  sendResponse(500, 'Internal Server Error', $r_body, 'application/' . $mode);
	  exit();
	}
	
	// Get passhash from DB to make the authentication
        $authentication_query = pg_query($db, "SELECT passhash FROM aptoide_user WHERE token = '{$token}'");
	$passhash = pg_fetch_result($authentication_query, 0, 0);

	if(empty($passhash))
	{
	  pg_close($db);
	  $response = createResponse('FAIL', '', 'Forbidden, access allowed only to Aptoide users');
	  $response = formatResponse($response, $mode);
	  sendResponse(403, 'Forbidden', $response, 'application/' . $mode);
	  exit();
	}	

	// Check the request's authenticity and integrity using HMAC with passHash as private key                                              
	if($signature == hash_hmac('sha1', $token . $device_id . $device_model, $passhash))
	{
	  // Register device in Aptoide Database, if its a valid aptoide user token and device its not registered yet
	  $register_query = pg_query($db, "INSERT INTO device (device_id, model, registered_on) SELECT '{$device_id}', '{$device_model}', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM device WHERE device_id = '{$device_id}') AND EXISTS (SELECT 1 FROM aptoide_user WHERE token = '{$token}')");
	
	  $queue_id = generateQueueId($token, $device_id);

	  // Associate device with user aptoide account
	  $association_query = pg_query($db, "INSERT INTO user_device (device_id, usertoken, queue_id, association_date) SELECT '{$device_id}', '{$token}', '{$queue_id}', CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM user_device WHERE device_id = '{$device_id}' AND usertoken = '{$token}')");
	
	} 
	else 
	{
	  pg_close($db);
	  $response = createResponse('FAIL', '', 'Request Unauthorized');
	  $response = formatResponse($response, $mode);
	  sendResponse(401, 'Unauthorized', $response, 'application/' . $mode);
	  exit();
	}
	
	pg_close($db);
	
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
  $response = createResponse('FAIL', '' , 'Invalid Request');
  $response = formatResponse($response, $mode);
  sendResponse(400, 'Bad Request', 'Invalid Request');
}


function generateQueueId($token, $device_id)
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
