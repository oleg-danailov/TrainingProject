package com.estafet.training.service;

import com.estafet.training.processor.FileNameProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.Properties;

public class HTTPRequestToFileServiceTest extends CamelTestSupport {
    Processor filenameProcessor = new FileNameProcessor();
    private int availablePort =0;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new HTTPRequestToFileService();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry= super.createRegistry();
        jndiRegistry.bind("filenameProcessor", filenameProcessor);
        return jndiRegistry;
    }
    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties p = super.useOverridePropertiesWithPropertiesComponent();

        if (p == null)
            p = new Properties();

        availablePort = AvailablePortFinder.getNextAvailable();
        p.setProperty("jetty.port", String.valueOf(availablePort));

        return p;
    }
    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        // setup the properties component to use the production file
        PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        prop.setLocation("classpath:props.properties");

        return context;
    }
    @Test
    public void testRouteWithMock() throws Exception {
        MockEndpoint endpoint = getMockEndpoint("mock:file");
        RouteDefinition route = context.getRouteDefinition("http-to-file-route");
        route.adviceWith(context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                interceptSendToEndpoint("file:*")
                        .skipSendToOriginalEndpoint()
                        .to("mock:file");
            }
        });
        context.start();
        String requestBody = "test message";

        template.sendBody("jetty:http://localhost:"+ availablePort +"/training/requesttofile", requestBody);

        endpoint.expectedMessageCount(1);
        assertMockEndpointsSatisfied();

        Exchange exchange = endpoint.getExchanges().get(0);
        String body = exchange.getIn().getBody(String.class);

        assertEquals("Messages are different",requestBody, exchange.getIn().getBody(String.class));


    }
}