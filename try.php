<?php
if(isset($_POST['username']) && isset($_POST['device_imei']))
{
	$username = $_POST['username'];

	$imei = $_POST['device_imei'];

	print $username ." with device ". $imei;
}
else 
{
	print "NÃO ESTA A RECONHER OS PARAMETROS";
}
?>
