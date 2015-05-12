package carrental.routing;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Thomas on 11.05.2015.
 */
@Component
public class DemoInvoiceRouter extends RouteBuilder {
    @Override
    public void configure() {
        from("activemq:queue:invoices").process(new Processor() {
            public void process(Exchange e) {
                System.out.println("Invoice received: "+ e.getIn().getBody());
            }
        });
    }
}
