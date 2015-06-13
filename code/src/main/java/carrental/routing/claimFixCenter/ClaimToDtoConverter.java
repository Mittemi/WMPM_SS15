package carrental.routing.claimFixCenter;

import carrental.model.pickupPoint.Claim;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Constantin on 13.06.2015.
 */
public class ClaimToDtoConverter implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Long carId = exchange.getIn().getHeader("carId",Long.class);
        ClaimDTO claimdto = new ClaimDTO(exchange.getIn().getBody(Claim.class),carId);
        exchange.getIn().setBody(claimdto);
    }
}
