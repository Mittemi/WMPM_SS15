package carrental.beans.reservation;

import carrental.model.reservation.Reservation;
import carrental.repository.reservation.ReserveRepository;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by Thomas on 23.05.2015.
 * Simulates the customer arrival at the receptions point, after the customer had done the reservation via web
 * interface. The receptionist therefore waits at the reception and receives the customer. Secondly, the receptionist
 * tells the ESB (e.g. via hitting a button in the reservation application), that the customer will now come to the
 * Pickup Point. [....The Pickup Point will then be informed by the ESB]
 */
@Component
public class CustomerArrivalSimulation{

    @Autowired
    private ReserveRepository reservationRepo;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "waitForCustomerArrivalQueue")
    public void simulateArrival(String reservationConfirmation) {
        int waitTime = Math.abs(new Random().nextInt() % 5000);
        try {
            Thread.sleep(waitTime);
        }
        catch (InterruptedException ex) { ex.printStackTrace();}
        String content = reservationConfirmation.substring(reservationConfirmation.indexOf(": ") + 1);
        content = content.trim();
        Reservation reservation = reservationRepo.findOne(Long.parseLong(content));
        System.out.println("Reservation: Customer " + reservation.getCustomer().getName() + " reserved " +
                reservation.getCar().getDescription() +" via web application and is now entering the store to pick it" +
                " up.");
        jmsTemplate.convertAndSend("waitingForCustomerDoneQueue", reservationConfirmation);
    }
}
