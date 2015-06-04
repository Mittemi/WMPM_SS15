package carrental.beans.claimFixCenter;

import carrental.model.pickupPoint.Claim;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Constantin on 03.06.2015.
 */
public class PaintStation implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Claim claim =exchange.getIn().getBody(Claim.class);
        System.out.println("PaintStation fixed "+claim.getDescription() + " for car with ID="+exchange.getIn().getHeader("carId)") );
    }
}
