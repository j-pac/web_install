<?php


if(isset($_POST['username']) && isset($_POST['device_imei']))
{

	print "Recebeu bem os parametros!";
	$username = $_POST['username'];
	$d_imei = $_POST['device_imei'];
}
/*
	// CONNECT TO DATABASE
	$db = pg_connect("host=localhost port=5432 dbname=web_install user=postgres password="blfa5661");

	// GENERATING A UNIQUE QUEUE NAME WITH SHA1
	$queue_name = generateQueueName($username, $d_imei);

	// IT'S ONLY INSERTING IN QUEUE TABLE
	$query = "INSERT INTO queue VALUES ($d_imei, $username, $queue_name)";

	$result = pg_query($query);

	echo $result; */
else
{
	print "Nao esta a receber os parametros!";
}
private function generateQueueName($username, $d_imei)
	{
		$hash_input = $username . $d_imei;

		$hash = sha1($hash_input);
		echo $hash;

		return $hash;
	}
?>
