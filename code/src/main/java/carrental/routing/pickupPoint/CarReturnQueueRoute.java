package carrental.routing.pickupPoint;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * Created by Michael on 14.05.2015.
 */
@Component
public class CarReturnQueueRoute extends RouteBuilder {

    // milliseconds
    static final int MIN_TIME_CAR_RETURN = 3000;
    static final int MAX_TIME_CAR_RETURN = 6000;

    @Override
    public void configure() throws Exception {
        from("timer://test?period=5000").setBody().constant("Car: 1").log("Message: ${body}")./*from("direct:pickupPoint").*/to("seda:pickupPoint.CarReturn.internal");

        from("seda:pickupPoint.CarReturn.internal").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Random random = new Random(new Date().getTime());
                Thread.sleep(random.nextInt(MAX_TIME_CAR_RETURN - MIN_TIME_CAR_RETURN)+ MIN_TIME_CAR_RETURN);
                System.out.println("Customer arrived to return car: " + exchange.getIn().getBody());
            }
        }).to("seda:pickupPoint.CarReturn");
    }
}
