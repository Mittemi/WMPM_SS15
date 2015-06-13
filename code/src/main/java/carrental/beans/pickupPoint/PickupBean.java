package carrental.beans.pickupPoint;

import carrental.Constants;
import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ClaimType;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.Reservation;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael on 23.05.2015.
 * Bean simulating the application at the pickup point.
 *
 * Creates a PickupProtocol and adds a random number of claims.
 */
@Component
public class PickupBean {


    private void println(String str) {
        System.out.println("PickupPoint: " + str);
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "createPickupProtocolQueue")
    public void showCar(Reservation reservation) {

        Random random = new Random(new Date().getTime());

        PickupProtocol pickupProtocol = new PickupProtocol();
        pickupProtocol.setReservation(reservation);

        println("Showing the car to the customer...");
        pickupProtocol.setClaims(generateClaims());
        if(pickupProtocol.getClaims().size() > 0) {
            println("Claims:");
            for (Claim claim : pickupProtocol.getClaims()) {
                println(" - " + claim.toString());
            }
        }
        pickupProtocol.setPickupDate(new Date());

        if(random.nextInt(100) < 30) {
            pickupProtocol.setCanceledPickup(true);
            println("Customer refuses to take this car! Cancel the pickup...");
        }else {
            println("Pickup done, have a safe trip...");
        }

        jmsTemplate.convertAndSend("createPickupProtocolDoneQueue", pickupProtocol);
    }

    public static List<Claim> generateClaims() {

        List<Claim> claimList = new LinkedList<>();
        Random random = new Random(new Date().getTime());

        boolean[] used = new boolean[5];

        while(random.nextBoolean()) {

            switch (random.nextInt(used.length + 1)) {
                case 0:
                    if(!used[0]) {
                        claimList.add(new Claim(ClaimType.Cleaning, "Car requires cleaning, still dirty!"));
                    }
                    used[0] = true;
                    break;
                case 1:
                    if(!used[1]) {
                        claimList.add(new Claim(ClaimType.Electrical, "Lights are not working"));
                    }
                    used[1] = true;
                    break;
                case 2:
                    if(!used[2]) {
                        claimList.add(new Claim(ClaimType.Mechanical, "Control lamp indicates error!"));
                    }
                    used[2] = true;
                    break;
                case 3:
                    if(!used[3]) {
                        claimList.add(new Claim(ClaimType.Refilling, "Someone forgot to refill!"));
                    }
                    used[3] = true;
                    break;
                case 4:
                    if(!used[4]) {
                        claimList.add(new Claim(ClaimType.Paintwork, "Don't know what they thought when they accepted this car during return!"));
                    }
                    used[4] = true;
                    break;
                default:
                    break;
            }
        }
        return claimList;
    }
}
