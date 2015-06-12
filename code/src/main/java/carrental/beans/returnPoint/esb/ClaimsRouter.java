package carrental.beans.returnPoint.esb;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

/**
 * Created by Michael on 08.06.2015.
 */
public class ClaimsRouter implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        ReturnProtocol returnProtocol = exchange.getIn().getBody(ReturnProtocol.class);
        List<Claim> claimsList = returnProtocol.getClaims();

        //If claimList empty, then set Header to noClaims
        if(claimsList.isEmpty())
        {
            exchange.getIn().setHeader("recipients","direct:billingPoint");
            System.out.println("ReturnPoint: Car Inspection for CarId = "+ returnProtocol.getReservation().getCarId()+" : No Claims --> Billing Point");
        }
        else {
            exchange.getIn().setHeader("recipients", "seda:queue:claimFixCenter,seda:queue:claimFixAggregationPoint");
            System.out.println("ReturnPoint: Car Inspection for CarId = "+ returnProtocol.getReservation().getCarId()+" : Claims found --> ClaimFixCenter ");
        }

        exchange.getIn().setHeader("claimsCnt+Protocol",claimsList.size()+1);
    }
}
