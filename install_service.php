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
  $query1 = pg_query($db, "SELECT passhash FROM aptoide_user WHERE token = '{$token}'");
  $pass_hash = pg_fetch_result($query1, 0, 0);
  
  if(empty($pass_hash))
  {
    pg_close($db);
    sendResponse(403, 'Forbidden', 'Forbidden, access allowed only to Aptoide users');
    exit();
  }

  // Check the request's authenticity and integrity using HMAC with passHash as private key
  if($signature == hash_hmac('sha1', $myapp, $pass_hash)) 
  {  
    $query2 = pg_query($db, "SELECT queue_id FROM user_device WHERE device_id = '{$device_id}' AND usertoken = '{$token}'");
    $queue_id = pg_fetch_result($query2, 0, 0);
  }
  else 
  {    
    pg_close($db);
    sendResponse(401, 'Unauthorized', 'Request Unauthorized');
    exit();
  }
  
  
  if(isset($queue_id)) 
  {
    $my_app = new SimpleXMLElement($myapp);
    $app_md5 = pg_escape_string(utf8_encode($my_app->getapp->md5sum)); 
    $app_name = pg_escape_string(utf8_encode($my_app->getapp->name)); 
    $app_size = pg_escape_string(utf8_encode($my_app->getapp->intsize)); 
    $app_pname = pg_escape_string(utf8_encode($my_app->getapp->pname)); 
    $app_server = pg_escape_string(utf8_encode($my_app->newserver->server)); 
    
    // DUMMY QUERY - add app's general information to db, simulating aptoide apps database
    $query3 = pg_query("INSERT INTO app (md5sum, name, size, pname, server) SELECT '{$app_md5}', '{$app_name}', '{$app_size}', '{$app_pname}', '{$app_server}' WHERE NOT EXISTS (SELECT 1 FROM app WHERE md5sum = '{$app_md5}')");
    
    // History information: associate user's requested app information to user_device_app in database
    $query4 = pg_query("INSERT INTO user_device_app (device_id, usertoken, app_md5sum, web_request_date, app_name) VALUES ('{$device_id}', '{$token}', '{$app_md5}', CURRENT_TIMESTAMP, '{$app_name}')");
    
  }
  else
  {
    pg_close($db);
    sendResponse(404, 'Not Found', 'Resource Not Found, the device is not registered'); 
    exit();
  }

  pg_close($db);
 
  // add a HMAC signature to myapp
  $myapp = createMyappSignature($myapp, $pass_hash);

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
