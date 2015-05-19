package carrental.routing.pickupPoint;

import carrental.model.pickupPoint.PickupProtocol;
import carrental.repository.pickupPoint.ReservationRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * Created by Michael on 14.05.2015.
 */
@Component
/**
 * Triggers a car return after a random amount of time.
 *
 * INPUT-QUEUE: seda:pickupPoint.CarReturn.internal
 * OUTPUT-QUEUE: seda:queue:carToInspectQueue
 */
public class CarReturnQueueRoute extends RouteBuilder {

    // milliseconds
    static final int MIN_TIME_CAR_RETURN = 300;
    static final int MAX_TIME_CAR_RETURN = 2000;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void configure() throws Exception {

        //TODO: queue full or stuff like that ???

        from("seda:pickupPoint.CarReturn.internal").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Random random = new Random(new Date().getTime());
                Thread.sleep(random.nextInt(MAX_TIME_CAR_RETURN - MIN_TIME_CAR_RETURN) + MIN_TIME_CAR_RETURN);
                System.out.println("Customer arrived to return car: " + exchange.getIn().getBody());
            }
        }).process(p -> {
            p.getOut().setHeader("carId", ((PickupProtocol)p.getIn().getBody()).getReservation().getCarId());
        }).to("seda:queue:carToInspectQueue");
    }
}
