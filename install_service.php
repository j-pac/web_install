<?php

require_once 'Publisher.php';

if(isset($_REQUEST['token']) && isset($_REQUEST['device_id']) && isset($_REQUEST['myapp']))
{

  // Request Parameters escaped to avoid SQL injection
  $token = pg_escape_string(utf8_encode($_REQUEST['token']));
  $device_id = pg_escape_string(utf8_encode($_REQUEST['device_id']));
  $myapp = $_REQUEST['myapp'];
 
  // Database connection
  $db = pg_connect("host=localhost port=5432 dbname=aptoide user=postgres password=godIsAProgrammer");
  if(empty($db))
  {
      sendResponse(500, 'Internal Server Error', 'Error connecting to database');
      exit();
  }

  
  // QUERYNG

/*  $pstm = "SELECT queue_name FROM queue WHERE device_imei=$1 AND username=$2";
    pg_prepare('install_query', $pstm); 

    $result = pg_execute('install_query', array($d_imei, $username)); */

  $result = pg_query($db, "SELECT queue_id FROM user_device WHERE device_id = '{$device_id}' AND usertoken = '{$token}'");

  pg_close($db);

  $queue_id = pg_fetch_result($result, 0, 0);

  if(empty($queue_id)) 
  {
    sendResponse(404, 'Not Found', 'Resource Not Found'); 
    exit();
  }

  //ir buscar a passHash do user à BD
  $pass_hash;

  //$myapp = addMyappSignature($myapp, $pass_hash);

  // PUBLISHING
  $publisher = new Publisher();

  $publisher->publish_message(installSuperMario(), $queue_id);

  $publisher->close();
  
  sendResponse(200, 'OK', 'Message sent with success');

} 
else 
{
  sendResponse(400, 'Bad Request', 'Invalid Request');
}


function installSuperMario() {
  $message = "<myapp><getapp><name>Super Mario Bros</name><get>http://pool.apk.bazaarandroid.com/acidaus/com-andro-romba-7-533729-7cec69573cf75313d1ec13ec2e378cca.apk</get><pname>com.andro.romba</pname><md5sum>7cec69573cf75313d1ec13ec2e378cca</md5sum><intsize>5774910</intsize></getapp><newserver><server>http://acidaus.bazaarandroid.com/</server></newserver></myapp>";
  return $message;
}

function installMinecraft() {
  $message = "<myapp><getapp><name>Minecraft PE</name><get>http://pool.apk.aptoide.com/hasan5654/com-mojang-minecraftpe-30006010-3728446-a6d22e16f992c5205f14590489c266b2.apk</get><pname>com.mojang.minecraftpe</pname><md5sum>a6d22e16f992c5205f14590489c266b2</md5sum><intsize>6579362</intsize></getapp><newserver><server>http://hasan5654.store.aptoide.com</server></newserver></myapp>";
  return $message;
}

// add a signature to the message, protecting it against message tampering and to authenticate it in the client
function AddMyappSignature($myapp, $private_key) {
  $message_sign = computeHmacSignature($message, $private_key);

  $myapp_xml = new SimpleXMLElement($myapp);
  $myapp_xml->addChild('signature', $message_sign);
  return $myapp_xml;
}

function computeHmacSignature($data, $private_key) {
  $hmac_hash = hash_hmac('sha1', $data, $private_key);
  return base64_encode($hmac_hash);
}

function sendResponse($status_code = 200, $status_message = 'OK', $body = '', $content_type = 'text/html')
{
  $status_header ='HTTP/1.1 ' . $status_code . ' ' . $status_message;
  header($status_header);
  header('Content-type: ' . $content_type);
  echo $body;
}

?>
