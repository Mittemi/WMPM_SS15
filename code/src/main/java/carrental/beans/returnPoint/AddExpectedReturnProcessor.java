package carrental.beans.returnPoint;

import carrental.model.pickupPoint.PickupProtocol;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Constantin on 16.05.2015.
 */
public class AddExpectedReturnProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        PickupProtocol protocol = exchange.getIn().getBody(PickupProtocol.class);
        if(protocol!=null)
        {
            CarQueue.listOfPickups.add(protocol);
            System.out.println("Cars expected for return: ");
            for(PickupProtocol pickup:CarQueue.listOfPickups)
            {
                System.out.println("\n"+"Car ID: "+pickup.getReservation().getCarId());
            }
        }
    }
}
