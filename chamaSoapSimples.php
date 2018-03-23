<?php  
   // connect to soap server
    $calcCliente = new SoapClient('http://127.0.0.1:9876/calc?wsdl');
 
    // log in
    $resp = $calcCliente->soma(122, 10);
 
    var_dump($resp);
    echo ("RESP = " . $resp );
