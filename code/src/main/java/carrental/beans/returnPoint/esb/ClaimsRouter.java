package carrental.beans.returnPoint.esb;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ReturnProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

/**
 * Created by Constantin & Michael on 08.06.2015.
 */
public class ClaimsRouter implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        ReturnProtocol returnProtocol = exchange.getIn().getBody(ReturnProtocol.class);
        List<Claim> claimsList = returnProtocol.getClaims();

        if(claimsList.isEmpty())
        {
            exchange.getIn().setHeader("recipients","direct:billingPoint");
            System.out.println("ESB (RP): Car with ID="+ returnProtocol.getReservation().getCarId()+" : No Claims --> Billing Point");
        }
        else {
            exchange.getIn().setHeader("recipients", "seda:queue:claimFixCenter");
            exchange.getIn().setHeader("protocolType",ReturnProtocol.class.getName().toString());
            System.out.println("ESB (RP) Car with ID= "+ returnProtocol.getReservation().getCarId()+" : Claims found --> ClaimFixCenter ");
        }

        exchange.getIn().setHeader("claimsCnt+Protocol",claimsList.size()+1);
    }
}
