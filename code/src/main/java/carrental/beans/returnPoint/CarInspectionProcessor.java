package carrental.beans.returnPoint;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Constantin on 14.05.2015.
 */
public class CarInspectionProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        ReturnProtocol protocol = exchange.getIn().getBody(ReturnProtocol.class);
        System.out.println("Retrun Protocol arrived, ID="+protocol.getId()+"Claims: ");
        for(Claim claim: protocol.getClaims())
        {
            System.out.println("Type: "+ claim.getClaimType()+"Description: "+claim.getDescription());
        }
    }
}
