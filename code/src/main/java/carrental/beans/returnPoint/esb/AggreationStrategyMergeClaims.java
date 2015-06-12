package carrental.beans.returnPoint.esb;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.List;

/**
 * Created by Michael on 12.06.2015.
 */
public class AggreationStrategyMergeClaims implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if(oldExchange == null) {
            return newExchange;
        }

        Exchange claimsExchange = null;
        Exchange returnProtocolExchange = null;


        //we don't know the order which we are going to receive the exchanges in.

        Object body = oldExchange.getIn().getBody();

        if(body instanceof ReturnProtocol) {
            returnProtocolExchange = oldExchange;
            claimsExchange = newExchange;
        } else  {
            returnProtocolExchange = newExchange;
            claimsExchange = oldExchange;
        }

        ReturnProtocol returnProtocol = returnProtocolExchange.getIn().getBody(ReturnProtocol.class);
        List<Claim> claimsList = (List<Claim>) claimsExchange.getIn().getBody();


        for (Claim claim : claimsList) {
            if(!returnProtocol.getClaims().stream().anyMatch(x->x.getClaimType() == claim.getClaimType()))
                returnProtocol.getClaims().add(claim);
        }

        return returnProtocolExchange;
    }
}
