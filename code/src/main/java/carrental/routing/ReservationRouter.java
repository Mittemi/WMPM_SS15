package carrental.routing;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Thomas on 12.05.2015.
 */
@Component
public class ReservationRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("activemq:queue:reservationDoneQueue").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("Message passed from reservation system to pickup point: "+exchange.getIn().getBody());
            }
        }).to("activemq:queue:pickupInitiatedQueue");
    }
}
