<?php

require_once 'Publisher.php';

if(isset($_REQUEST['token']) && isset($_REQUEST['device_id']) && isset($_REQUEST['myapp'])))
{

  $token = $_REQUEST['token'];
  $d_imei = $_REQUEST['device_id'];
  $myapp = $_REQUEST['message'];
  //  $format = ($_REQUEST['format'] == 'json') ? 'json' : 'xml';

  // ESTABLISH CONNECTIONS WITH POSTGRES AND RABBITMQ
  $db = pg_connect("host=localhost port=5432 dbname=register user=postgres password=godIsAProgrammer") or die("could not connect");

  $publisher = new Publisher();

  // QUERYNG
  $pstm = "SELECT queue_name FROM queue WHERE device_imei=$1 AND username=$2";
  pg_prepare('install_query', $pstm);

  $result = pg_execute('install_query', array($d_imei, $username));

  if(empty($result)) 
  {
    //    send
      return false;
  }

  $queue_name = pg_fetch_result($result, 0, 0);


  //ir buscar a passHash do user à BD
  $pass_hash = ;

  $myapp = addMyappSignature($myapp, $pass_hash);

  // PUBLISHING
  $publisher->publish_message($myapp, $queue_name);

  $publisher->close();
  pg_close($db);

  sendResponse();

  print "Install service closed!";

} 
else 
{
  sendResponse(400, 'Bad Request', 'Invalid Request');
}


function installSuperMario() {
  $message = "<myapp><getapp><name>GOSMS Mario Theme</name><get>http://pool.apk.aptoide.com/sjb/com-jb-gosms-webtheme-supermario-4-869178-83194762431315296bf0078c37fe4df7.apk</get><pname>com.jb.gosms.webtheme.supermario</pname><md5sum>83194762431315296bf0078c37fe4df7</md5sum><intsize>1116048</intsize></getapp><newserver><server>http://sjb.store.aptoide.com</server></newserver></myapp>";
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
