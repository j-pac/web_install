<?php

require_once 'Publisher.php';

if(isset($_REQUEST['username']) && isset($_REQUEST['device_imei']) && isset($_REQUEST['message']))
{

  $username = $_REQUEST['username'];
  $d_imei = $_REQUEST['device_imei'];
  $message = $_REQUEST['message'];

  // ESTABLISH CONNECTIONS WITH POSTGRES AND RABBITMQ
  $db = pg_connect("host=localhost port=5432 dbname=register user=postgres password=godIsAProgrammer") or die("could not connect");

  $publisher = new Publisher();

  // QUERYNG
  $pstm = "SELECT queue_name FROM queue WHERE device_imei=$1 AND username=$2";
  pg_prepare('install_query', $pstm);

  $result = pg_execute('install_query', array($d_imei, $username));
  $queue_name = pg_fetch_result($result, 0, 0);


  // PUBLISHING
  $publisher->publish_message(installSuperMario(), $queue_name);

  $publisher->close();
  pg_close($db);

  print "Install service closed!";

} 


function trySendMyAPPXML()
{
  $message =  '<?xml version="1.0" encoding="UTF-8"?>'.
	'<myapp>'.
	'<getapp>'.
	'<name>Real Football 2012</name>'.
	'<get>http://pool.apk.aptoide.com/teresa-deus/com-gameloft-android-anmp-gloftr2hm-154-3359682-928b5a4f4c19bf90aa2f89993783e0c9.apk</get>'.
	'<pname>com.gameloft.android.ANMP.GloftR2HM</pname>'.
	'<md5sum>928b5a4f4c19bf90aa2f89993783e0c9</md5sum>'.
	'<intsize>14093631</intsize>'.
	'</getapp>'.
	'<newserver>'.
	'<server>http://teresa-deus.store.aptoide.com</server>'.
	'</newserver>'.
	'<obb>'.
	'<main_path>http://pool.obb.aptoide.com/teresa-deus/main-153-com-gameloft-android-anmp-gloftr2hm-1274059296580c311667911e6893fc1f.obb</main_path>'.
	'<main_md5sum>1274059296580c311667911e6893fc1f</main_md5sum>'.
	'<main_filesize>404556221</main_filesize>'.
	'<main_filename>main.153.com.gameloft.android.ANMP.GloftR2HM.obb</main_filename>'.
	'</obb>'.
	'</myapp>';

  return $message;
}

function installSuperMario() {
  $message = "<myapp><getapp><name>GOSMS Mario Theme</name><get>http://pool.apk.aptoide.com/sjb/com-jb-gosms-webtheme-supermario-4-869178-83194762431315296bf0078c37fe4df7.apk</get><pname>com.jb.gosms.webtheme.supermario</pname><md5sum>83194762431315296bf0078c37fe4df7</md5sum><intsize>1116048</intsize></getapp><newserver><server>http://sjb.store.aptoide.com</server></newserver></myapp>";
  return $message;
}

// add a signature to the message, protecting it against message tampering and to authenticate it in the client
function AddMessageSignature($message, $private_key) {
  

}

function computeHmacSignature() {
}

?>
