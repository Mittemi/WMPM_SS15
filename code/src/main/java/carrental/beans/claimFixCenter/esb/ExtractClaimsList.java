package carrental.beans.claimFixCenter.esb;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;

import java.util.List;

/**
 * Created by Constantin on 04.06.2015.
 */
public class ExtractClaimsList implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        ReturnProtocol protocol = exchange.getIn().getBody(ReturnProtocol.class);
        List<Claim> claimsList = protocol.getClaims();
        System.out.println("Return Protocol arrived at ClaimFixCenter for car with ID="+protocol.getReservation().getCarId()+" Claims to Fix: ");
        for(Claim claim: protocol.getClaims())
        {
            System.out.println("Type: "+ claim.getClaimType()+" Description: "+claim.getDescription());
        }
        exchange.getIn().setBody(claimsList);
    }
}
