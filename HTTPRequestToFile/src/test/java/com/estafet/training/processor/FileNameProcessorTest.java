package com.estafet.training.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Created by estafet on 28/09/15.
 */
public class FileNameProcessorTest extends CamelTestSupport{
    @Test
    public void fileNameHeaderExist() throws Exception {

        Exchange exchange = new DefaultExchange(context);
        Processor processor = new FileNameProcessor();
        processor.process(exchange);
        assertNotNull("Header FILE_NAME is not set", exchange.getIn().getHeader(Exchange.FILE_NAME));

    }
    @Test
    public void testUniqueName() throws Exception {
        Processor processor = new FileNameProcessor();

        Exchange firstExchange = new DefaultExchange(context);
        processor.process(firstExchange);

        Exchange secondExchange = new DefaultExchange(context);
        processor.process(secondExchange);

        assertNotEquals("Filename should be unique", firstExchange.getIn().getHeader(Exchange.FILE_NAME), secondExchange.getIn().getHeader(Exchange.FILE_NAME));
    }

}
