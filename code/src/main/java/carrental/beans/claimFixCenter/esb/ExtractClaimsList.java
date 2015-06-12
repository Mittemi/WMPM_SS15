package carrental.beans.claimFixCenter.esb;

import java.util.List;

import org.apache.camel.Exchange;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;

/**
 * Created by Constantin on 04.06.2015.
 */
public class ExtractClaimsList implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
    	ReturnProtocol returnProtocol = exchange.getIn().getBody(ReturnProtocol.class);
        List<Claim> claimsList = null;
        if (returnProtocol != null) {
        	claimsList = returnProtocol.getClaims();
        	System.out.println("Return Protocol arrived at ClaimFixCenter for car with ID="+returnProtocol.getReservation().getCarId()+" Claims to Fix: ");
        } else {
        	PickupProtocol pickupProtocol = exchange.getIn().getBody(PickupProtocol.class);
        	claimsList = pickupProtocol.getClaims();
        	System.out.println("Pickup Protocol arrived at ClaimFixCenter for car with ID="+pickupProtocol.getReservation().getCarId()+" Claims to Fix: ");
        }
        for(Claim claim: claimsList)
        {
            System.out.println("Type: "+ claim.getClaimType()+" Description: "+claim.getDescription());
        }
        exchange.getIn().setBody(claimsList);
    }
}
