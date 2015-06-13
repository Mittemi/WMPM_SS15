package carrental.beans.returnPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import carrental.model.pickupPoint.ReturnProtocol;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Constantin on 13.06.2015.
 */
@Component
public class ReturnPointBean {
    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = "returnPoint.expectedReturns")
    public void AddExpectedReturn(ReturnProtocol returnProtocol) {

        CarQueue.listOfPickups.add(returnProtocol.getReservation().getCarId());

        System.out.println("Return Point: New car queued for return by Pickup Point. Cars expected for return: ");
        for(Long pickup:CarQueue.listOfPickups) {
            System.out.println("\n" + "  -- Car ID: " + pickup);
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
        CarQueue.listOfPickups.remove(returnProtocol.getReservation().getCarId());


        // required to simulate the csv generation for claims
        CarQueue.listForClaims.add(returnProtocol.getReservation());

        System.out.println("Car with id " + returnProtocol.getReservation().getCarId() + " arrived at return point");
        jmsTemplate.convertAndSend("carToInspectQueue", returnProtocol);
    }
}
