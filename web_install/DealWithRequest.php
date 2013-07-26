<?php
class DealWithRequest extends Thread {
      
      private $socket;
      private $publisher;
      

      public function __construct($socket, $publisher) {
      	     $this->socket = $socket;
      	     $this->publisher = $publisher;
      }

      public function run() {
      	     

      }
}

?>