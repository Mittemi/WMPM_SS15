package carrental.beans.claimFixCenter.esb;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;

import java.util.List;

/**
 * Created by Constantin on 04.06.2015.
 */
public class ExtractClaimsListFromProtocol implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        if(exchange.getIn().getBody() instanceof ReturnProtocol)
        {
            ReturnProtocol protocol = exchange.getIn().getBody(ReturnProtocol.class);
            List<Claim> claimsList = protocol.getClaims();
            System.out.println("ESB (ClaimFixCenter):Return Protocol arrived for car with ID=" + protocol.getReservation().getCarId() + " Claims to Fix: ");
            for (Claim claim : protocol.getClaims()) {
                System.out.println("Type: " + claim.getClaimType() + " Description: " + claim.getDescription());
            }
            exchange.getIn().setBody(claimsList);
        }
        if(exchange.getIn().getBody() instanceof PickupProtocol)
        {
            PickupProtocol protocol = exchange.getIn().getBody(PickupProtocol.class);
            List<Claim> claimsList = protocol.getClaims();

            System.out.println("ESB (ClaimFixCenter):Pickup Protocol arrived for car with ID=" + protocol.getReservation().getCarId() + " Claims to Fix: ");
            for (Claim claim : protocol.getClaims()) {
                System.out.println("Type: " + claim.getClaimType() + " Description: " + claim.getDescription());
            }
            exchange.getIn().setBody(claimsList);
        }
    }
}
