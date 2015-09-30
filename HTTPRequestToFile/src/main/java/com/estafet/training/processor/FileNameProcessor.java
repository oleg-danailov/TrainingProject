package com.estafet.training.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.UUID;

/**
 * Created by estafet on 28/09/15.
 */
public class FileNameProcessor implements Processor{
    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = UUID.randomUUID().toString();
        exchange.getIn().setHeader(Exchange.FILE_NAME, fileName +".txt");
    }
}
