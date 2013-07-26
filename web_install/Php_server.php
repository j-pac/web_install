<?php

function __autoload($class_name) {
	 include $class_name . '.php';
}

class Php_server {
      
      //Config variables
      $host = "127.0.0.1";
      $port = 

      private $socket_server;
      private $publisher;
      
      

      public function __construct() {
      	     this->publisher = new Publisher();
	     
	     this->socket_server = socket_create(AF_INET, SOCK_STREAM, 0) or die("[".date('Y-m-d H:i:s')."] Could not create socket\n");

	     socket_bind($socket_server, $host, $port) or die("[".date('Y-m-d H:i:s')."] Could not bind to socket\n");	     	     

	     socket_listen($socket_server) or die("[".date('Y-m-d H:i:s')."] Could not set up socket listener\n");
	     
	     emit_log("--> Server started at ".$host.":".$port);
        }

     private function emit_log($msg) {
     	     $msg = "[".date('Y-m-d H:i:s')."] ".$msg;
	     print($msg."\n");
     }


     private function start_serving() {
     	     
	     try 
	     {
	     while(True) {
	     		 if (($req_socket = socket_accept($socket_server)) ===	false) {
			    emit_log("socket_listen() failed: " . socket_strerror(socket_last_error($socket_server)) . "\n");
			 }
			 
			 new DealWithRequest($req_socket, $this->publisher)->start();
	     }
	     }
	     finally
	     {
		socket_close($this->socket_server);
		}		 
     }
     

     /*public function __destruct() {
     	    socket_close($this->socket_server);
     }*/

?>