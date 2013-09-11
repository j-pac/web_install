<?php

require_once 'Publisher.php';

if(isset($_REQUEST['token']) && isset($_REQUEST['device_id']) && isset($_REQUEST['myapp']) && isset($_REQUEST['signature']))
{
  //if(function_exists($_REQUEST['functionCall'])) {


  // Request Parameters escaped to avoid SQL injection
  $token = pg_escape_string(utf8_encode($_REQUEST['token']));
  $device_id = pg_escape_string(utf8_encode($_REQUEST['device_id']));
  $myapp = $_REQUEST['myapp'];
  $signature = $_REQUEST['signature'];
 
  // Database connection
  $db = pg_connect("host=localhost port=5432 dbname=aptoide user=postgres password=godIsAProgrammer");
  if(empty($db))
  {
      sendResponse(500, 'Internal Server Error', 'Error connecting to database');
      exit();
  }

  
  // Database querying
  $result1 = pg_query($db, "SELECT passhash FROM aptoide_user WHERE token = '{$token}'");
  $pass_hash = pg_fetch_result($result1, 0, 0);
  
  if(empty($pass_hash))
  {
    pg_close($db);
    sendResponse(403, 'Forbidden', 'Forbidden, access allowed only to Aptoide users');
    exit();
  }

  // Check the request's authenticity and integrity using HMAC with passHash as private key
  if($signature == hash_hmac('sha1', $myapp, $pass_hash)) 
  {  
    $result2 = pg_query($db, "SELECT queue_id FROM user_device WHERE device_id = '{$device_id}' AND usertoken = '{$token}'");
    $queue_id = pg_fetch_result($result2, 0, 0);
  }
  else 
  {    
    pg_close($db);
    sendResponse(401, 'Unauthorized', 'Request Unauthorized');
    exit();
  }

  pg_close($db);
  
  if(empty($queue_id)) 
  {
    sendResponse(404, 'Not Found', 'Resource Not Found, the device is not registered'); 
    exit();
  }
 
  // add a HMAC signature to myapp
  $myapp = createMyappSignature($myapp, $token);

  // Publish myapp to RabbitMq
  $publisher = new Publisher();

  $publisher->publish_message($myapp, $queue_id);

  $publisher->close();
  
  sendResponse(200, 'OK', 'App\'s install order sent with success');

 /* } 
  else
    {
      sendResponse(400, 'Bad Request', 'Invalid Functionality Called');
      }*/
} 
else 
{
  sendResponse(400, 'Bad Request', 'Invalid Request');
}



function listUserDevices($db, $usertoken) 
{

}

// add a HMAC signature to the message, protecting it against message tampering and to authenticate it in the client
function createMyappSignature($myapp, $private_key) { 
  $myapp = '<?xml version="1.0" encoding="UTF-8"?>' . $myapp;
  $message_signature = hash_hmac('sha1', $myapp, $private_key);
  $myapp = new SimpleXMLElement($myapp);
  $myapp->addChild('hmac', $message_signature);
  return $myapp->asXML();
}

function sendResponse($status_code = 200, $status_message = 'OK', $body = '', $content_type = 'text/html')
{
  $status_header ='HTTP/1.1 ' . $status_code . ' ' . $status_message;
  header($status_header);
  header('Content-type: ' . $content_type);
  echo $body;
}

?>
