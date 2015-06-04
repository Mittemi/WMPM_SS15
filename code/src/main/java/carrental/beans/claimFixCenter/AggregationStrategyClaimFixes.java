package carrental.beans.claimFixCenter;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ClaimType;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;

import java.util.Date;
import java.util.List;

/**
 * Created by Constantin on 04.06.2015.
 */
public class AggregationStrategyClaimFixes implements org.apache.camel.processor.aggregate.AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        // the first time there are no existing message and therefore
        // the oldExchange is null. In these cases we just return
        // the newExchange
        if (oldExchange == null) {
            return newExchange;
        }

        // and return it
        return oldExchange;
    }
}
