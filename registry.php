<?php


if(isset($_REQUEST['username']) && isset($_REQUEST['device_imei']))
{

	print "Recebeu bem os parametros!";
	$username = $_REQUEST['username'];
	$d_imei = $_REQUEST['device_imei'];

	/*$queue_name = generateQueueName($username, $d_imei);*/

	print "Ola";/*generateQueueName($username, $d_imei);*/

}

public function generateQueueName($username, $device_imei)
	{
		$hash_input = $username . $device_imei;
		print "Hash input = " . $hash_input;

		return sha1($hash_input);
	}



?>
