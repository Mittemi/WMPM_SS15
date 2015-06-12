package carrental.routing.pickupPoint;

import carrental.CarRentalConfig;
import carrental.beans.pickupPoint.esb.PullReservation;
import carrental.beans.pickupPoint.esb.ReservationTranslator;
import carrental.model.reservation.Reservation;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 14.05.2015.
 */
@Component
/**
 * Takes care of retrieving the reservation from the reservation system and storing it inside the pickup-point's mongodb.
 * Additionally it triggers the pickup-protocol creation
 * INPUT-QUEUE: activemq:queue:pickupInitiatedQueue
 * TRIGGERS:
 *  - direct:pickupPoint.PickupProtocol
 */
public class DatabaseSyncRoute extends RouteBuilder {

    @Autowired
    private CarRentalConfig config;

    @Override
    public void configure() throws Exception {

        // when a new entry is available in the queue it's body gets transformed into a simple id (reservation id)
        // later on the pollEnricher using the camel jpa component accesses the database to get the actual reservation
        // this reservation is stored using the camel mongodb component
        // additionally we trigger the actual pickup work (create pickup protocol)
        from("activemq:queue:pickupInitiatedQueue")
                .process(new Processor() {
                    public void process(Exchange exchange) {
                        Message in = exchange.getIn();
                        String content = in.getBody(String.class);
                        content = content.substring(content.indexOf(": ") + 1);
                        content = content.trim();
                        in.setBody(Long.parseLong(content));
                        System.out.println("ESB (PP): Transfer data from reservation to pickup point for id " + content);

                        //Map<String, Object> params = new HashMap<>();
                       // params.put("id", content);

                       // exchange.getOut().setHeader("params", params);
                    }
                })
                //.pollEnrich("jpa:" + Reservation.class.getName() + "?consumer.namedQuery=findById&consumeLockEntity=false&consumeDelete=false&consumer.parameters=" + header("params"))// pollEnrich
        //from("jpa:" + Reservation.class.getName() + "?consumeLockEntity=false&consumeDelete=false&consumer.delay=250").split(body())
                /* JPA won't accept parameters for the id during pollEnrich, therefore we need to use our custom implementation for this task */

                .bean(PullReservation.class)
                .bean(ReservationTranslator.class)
                .to("direct:insertReservationMongo");

        String mongoEndpointString = "mongodb:mongo?database=" + config.getPickupPoint().getMongo().getName() +"&collection=reservation&operation=save&writeResultAsHeader=true";

        from("direct:insertReservationMongo").process(p -> {
            System.out.println("ESB (PP): Save reservation to mongo db ");
        })
            .to(mongoEndpointString).to("direct:pickupPoint.PickupProtocol");
        }
    }
