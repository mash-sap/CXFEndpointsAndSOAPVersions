# CXF Endpoints & SOAP Versions
Starting endpoints with same URL but different SOAP versions is possible but is causing expected behavior
1) Run DemoApplication to startup the tomcat and camel context. There are two routes which register the same URL but with different SOAP versions (SOAP 1.1 vs 1.2)
2) SOAP 1.1 Route: Do a POST request to http://127.0.0.1:9900/cxf/myendpoint with following payload. Response should be HTTP 200 (response body comes from Route1:<b>FIRST</b>)<br>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"><soapenv:Body><n2:invoke xmlns:n2="http://cxf.component.camel.apache.org/">test</n2:invoke></soapenv:Body></soapenv:Envelope>

3) SOAP 1.2 Route: Do a POST request to http://127.0.0.1:9900/cxf/myendpoint with following payload. Response should be HTTP 200 (response body comes from Route1: <b>SECOND</b>)<br>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"><soap:Body><n2:invoke xmlns:n2="http://cxf.component.camel.apache.org/">test</n2:invoke></soap:Body></soap:Envelope>

3) Stop SOAP 1.2 endpoint: Do GET request to http://localhost:9900/route/start to stop Route2 which was serving <b>SECOND</b> route
4) retry step 2 and 3: Both URLS return now with HTTP 404. Unexpected behavior: SOAP 1.1 route should still be reachable
