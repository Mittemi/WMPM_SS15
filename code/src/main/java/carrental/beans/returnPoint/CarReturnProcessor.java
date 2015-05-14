package carrental.beans.returnPoint;

import carrental.model.pickupPoint.PickupProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Constantin on 14.05.2015.
 */
public class CarReturnProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        PickupProtocol protocol = exchange.getIn().getBody(PickupProtocol.class);
        System.out.println("Pickup Protocol arrived, ID="+protocol.getId());
    }
}
