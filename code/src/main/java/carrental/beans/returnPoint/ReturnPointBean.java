package carrental.beans.returnPoint;

import carrental.model.pickupPoint.PickupProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Constantin on 13.06.2015.
 */
@Component
public class ReturnPointBean {
    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = "returnPoint.expectedReturns")
    public void AddExpectedReturn(PickupProtocol protocol)
    {
            CarQueue.listOfPickups.add(protocol);
            System.out.println("Return Point: New car queued for return by Pickup Point. Cars expected for return: ");
            for(PickupProtocol pickup:CarQueue.listOfPickups) {
                System.out.println("\n" + "Car ID: " + pickup.getReservation().getCarId());
            }
    }
}
