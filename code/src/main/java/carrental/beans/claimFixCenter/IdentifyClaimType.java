package carrental.beans.claimFixCenter;

import carrental.model.pickupPoint.Claim;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Constantin on 04.06.2015.
 */
public class IdentifyClaimType implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Claim claim = exchange.getIn().getBody(Claim.class);
        exchange.getIn().setHeader("claimType",claim.getClaimType());
    }
}
