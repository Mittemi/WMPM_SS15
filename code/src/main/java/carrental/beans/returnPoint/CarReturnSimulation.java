package carrental.beans.returnPoint;

import carrental.model.pickupPoint.Claim;
import carrental.model.pickupPoint.ClaimType;
import carrental.model.pickupPoint.PickupProtocol;
import carrental.model.pickupPoint.Reservation;
import carrental.repository.reservation.CustomerRepository;
import carrental.repository.reservation.ReservationRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Constantin on 14.05.2015.
 */
@Component
public class CarReturnSimulation {

    @Autowired
    private ProducerTemplate producerTemplate;
    @Scheduled(fixedRate = 2500)
    public void carHandedOver()
    {
        PickupProtocol pickupProtocol = new PickupProtocol();
        pickupProtocol.setId(1L);

        LinkedList<Claim> claimsList = new LinkedList();
        Claim claim1= new Claim();
        claim1.setClaimType(ClaimType.Mechanical);
        claim1.setDescription("Engine exploded");
        claimsList.add(claim1);
        pickupProtocol.setClaims(claimsList);

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setCarId(1L);
        reservation.setCustomerId(1L);
        String testDate = "29-Apr-2015,13:00:14";
        DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss");
        Date date=new Date();
        try {
             date = formatter.parse(testDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reservation.setReservationDate(date);
        pickupProtocol.setReservation(reservation);
        pickupProtocol.setPickupDate(new Date());

        System.out.println("Simulated Car handed over");
        producerTemplate.sendBody("activemq:queue:CarHandedOverQueue",pickupProtocol);

    }

}
