package carrental.routing.reservation;

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
        from("activemq:queue:reservationDoneQueue").routeId("manualReservationRoute").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("ESB: Reservation message passed from reservation system to pickup point: " +
                        exchange.getIn()
                        .getBody());
            }
        }).to("activemq:queue:pickupInitiatedQueue");

        from("activemq:queue:reservationViaServiceDoneQueue").routeId("webApplicationReservationRoute").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("ESB: Reservation message received from reservation system and returned due to " +
                        "waiting for customer:" +
                        " " + exchange.getIn()
                        .getBody());
            }
        }).to("activemq:queue:waitForCustomerArrivalQueue");

        from("activemq:queue:waitingForCustomerDoneQueue").routeId("waitForCustomerDoneRoute").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                System.out.println("ESB: Reservation message passed from reservation system to pickup point (after " +
                        "web reservation): " +
                        exchange.getIn()
                        .getBody());
            }
        }).to("activemq:queue:pickupInitiatedQueue");
    }
}
