<?php
  $urlWsdl = 'http://127.0.0.1:8080/CalculatorWS/CalculatorWS?WSDL';
        try {
            $soapClient = new \SoapClient($urlWsdl, [
                'exceptions' => true
            ]);
            $requestData = [ 'i' => 14, 'j'=> 22];
            $response = $soapClient->add($requestData);
            var_dump($response);
            
        } catch (SoapFault $exception) {

            echo $exception->getMessage();

            return;
        }
