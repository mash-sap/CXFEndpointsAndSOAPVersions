# Issue 1: Deregistration of CXF Endpoints & SOAP Versions
Starting endpoints with same URL but different SOAP versions is possible but is causing expected behavior
1) Run DemoApplication to startup the tomcat and camel context. There are two routes which register the same URL but with different SOAP versions (SOAP 1.1 vs 1.2)
2) SOAP 1.1 Route: Do a POST request to http://127.0.0.1:9900/cxf/myendpoint with following payload. Response should be HTTP 200 (response body comes from Route1:<b>FIRST</b>)<br>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"><soapenv:Body><n2:invoke xmlns:n2="http://cxf.component.camel.apache.org/">test</n2:invoke></soapenv:Body></soapenv:Envelope>

3) SOAP 1.2 Route: Do a POST request to http://127.0.0.1:9900/cxf/myendpoint with following payload. Response should be HTTP 200 (response body comes from Route1: <b>SECOND</b>)<br>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"><soap:Body><n2:invoke xmlns:n2="http://cxf.component.camel.apache.org/">test</n2:invoke></soap:Body></soap:Envelope>

3) Stop SOAP 1.2 endpoint: Do GET request to http://localhost:9900/route/start to stop Route2 which was serving <b>SECOND</b> route
4) retry step 2 and 3: Both URLS return now with HTTP 404. Unexpected behavior: SOAP 1.1 route should still be reachable


# Issue 1: NullPointerException due to CounterRepository
1) Uncomment line 37-40 in my-camel.xml to enable the CounterRepository bean
2) Run DemoApplication to startup the tomcat and camel context. There are two routes which register the same URL but with different SOAP versions (SOAP 1.1 vs 1.2)
3) Call http://127.0.0.1:9900/cxf/myendpoint with SOAP 1.1 or SOAP 1.2 payload (see above): Unexpected behavior: HTTP 200 without a repsonse body
4) Check the logs and find NPE:
 
java.lang.NullPointerException: Cannot invoke "org.apache.cxf.endpoint.Endpoint.get(Object)" because "endpoint" is null
	at org.apache.cxf.management.interceptor.AbstractMessageResponseTimeInterceptor.getServiceCounterName(AbstractMessageResponseTimeInterceptor.java:126) ~[cxf-rt-management-3.4.2.jar:3.4.2]
	at org.apache.cxf.management.interceptor.AbstractMessageResponseTimeInterceptor.isServiceCounterEnabled(AbstractMessageResponseTimeInterceptor.java:163) ~[cxf-rt-management-3.4.2.jar:3.4.2]
	at org.apache.cxf.management.interceptor.ResponseTimeMessageInInterceptor.handleMessage(ResponseTimeMessageInInterceptor.java:40) ~[cxf-rt-management-3.4.2.jar:3.4.2]
	at org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:308) ~[cxf-core-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.MultipleEndpointObserver.onMessage(MultipleEndpointObserver.java:98) ~[cxf-core-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.http.AbstractHTTPDestination.invoke(AbstractHTTPDestination.java:265) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.servlet.ServletController.invokeDestination(ServletController.java:234) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:208) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:160) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.servlet.CXFNonSpringServlet.invoke(CXFNonSpringServlet.java:225) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.servlet.AbstractHTTPServlet.handleRequest(AbstractHTTPServlet.java:298) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.cxf.transport.servlet.AbstractHTTPServlet.doPost(AbstractHTTPServlet.java:217) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:652) ~[tomcat-embed-core-9.0.45.jar:4.0.FR]
	at org.apache.cxf.transport.servlet.AbstractHTTPServlet.service(AbstractHTTPServlet.java:273) ~[cxf-rt-transports-http-3.4.2.jar:3.4.2]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.6.jar:5.3.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.6.jar:5.3.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.6.jar:5.3.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.6.jar:5.3.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.6.jar:5.3.6]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.6.jar:5.3.6]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1707) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628) ~[na:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.45.jar:9.0.45]
	at java.base/java.lang.Thread.run(Thread.java:829) ~[na:na]

