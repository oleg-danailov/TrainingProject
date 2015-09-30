package com.estafet.training.service;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by estafet on 17/09/15.
 */
public class HTTPRequestToFileService extends RouteBuilder{
    @Override
    public void configure() throws Exception {
        from("jetty:http://{{jetty.host}}:{{jetty.port}}{{jetty.url}}").routeId("http-to-file-route")
                .convertBodyTo(String.class)
//                .streamCaching()
                .log(LoggingLevel.INFO, "Message recieved: ${body}")
                .processRef("filenameProcessor")
                .to("file:///u01/tmp/");
    }
}
