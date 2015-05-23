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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael on 23.05.2015.
 * Bean simulating the application at the pickup point.
 *
 * Creates a PickupProtocol and adds a random number of claims. Might
 */
@Component
public class PickupBean {

    @Autowired
    private ProducerTemplate producerTemplate;

    private Random random = new Random(new Date().getTime());


    public void showCar(Reservation reservation) {

        PickupProtocol pickupProtocol = new PickupProtocol();
        pickupProtocol.setReservation(reservation);

        System.out.println("Showing the car to the customer...");
        pickupProtocol.setClaims(generateClaims());
        if(pickupProtocol.getClaims().size() > 0) {
            System.out.println("Claims:");
            for (Claim claim : pickupProtocol.getClaims()) {
                System.out.println(" - " + claim.toString());
            }
        }
        pickupProtocol.setPickupDate(new Date());

        if(random.nextInt(100) < 10) {
            pickupProtocol.setCanceledPickup(true);
            System.out.println("Customer refuses to take this car! Cancel the pickup...");
            if(Constants.ENABLE_CANCEL_PICKUP) {
                producerTemplate.sendBody("direct:pickupPoint.cancel", pickupProtocol);
            }
        }else {
            System.out.println("Pickup done, have a safe trip...");
            if(Constants.ENABLE_CAR_RETURN) {
                producerTemplate.sendBodyAndHeader("seda:queue:carToInspectQueue", pickupProtocol, "carId", pickupProtocol.getReservation().getCarId());        //notify return process that another car is expected to return in future#
            }
        }
    }

    private List<Claim> generateClaims() {

        List<Claim> claimList = new LinkedList<>();

        boolean[] used = new boolean[5];

        while(random.nextBoolean()) {

            switch (random.nextInt(used.length + 1)) {
                case 0:
                    used[0] = true;
                    if(!used[0]) {
                        claimList.add(new Claim(ClaimType.Cleaning, "Car requires cleaning, still dirty!"));
                    }
                    break;
                case 1:
                    used[1] = true;
                    if(!used[1]) {
                        claimList.add(new Claim(ClaimType.Electrical, "Lights are not working"));
                    }
                    break;
                case 2:
                    used[2] = true;
                    if(!used[2]) {
                        claimList.add(new Claim(ClaimType.Mechanical, "Control lamp indicates error!"));
                    }
                    break;
                case 3:
                    used[3] = true;
                    if(!used[3]) {
                        claimList.add(new Claim(ClaimType.Refilling, "Someone forgot to refill!"));
                    }
                    break;
                case 4:
                    used[4] = true;
                    if(!used[4]) {
                        claimList.add(new Claim(ClaimType.Paintwork, "Don't know what they thought when they accepted this car during return!"));
                    }
                    break;
                default:
                    break;
            }
        }
        return claimList;
    }
}
