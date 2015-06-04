package carrental.beans.returnPoint;

import carrental.Constants;
import carrental.model.pickupPoint.*;
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
        if(Constants.ENABLE_CAR_RETURN) return; // no simulation use real one

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
        reservation.setCarId(CarSimulation.getNewCarId());
        reservation.setCustomerId(1L);
        String reservationDateString = "29-Apr-2015,13:00";
        String pickupDateString = "30-Apr-2015,13:00";
        DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm");
        Date reservationDate=new Date();
        Date pickupDate = new Date();
        try {
             reservationDate = formatter.parse(reservationDateString);
            pickupDate= formatter.parse(pickupDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reservation.setReservationDate(reservationDate);
        pickupProtocol.setReservation(reservation);
        pickupProtocol.setPickupDate(pickupDate);

        System.out.println("Simulated Car Handed over and Pickup protocol forwarded to Car Return Point");
        producerTemplate.sendBodyAndHeader("seda:queue:carToInspectQueue",pickupProtocol,"carId",pickupProtocol.getReservation().getCarId());


    }
    @Scheduled(fixedRate = 3000)
    public void carReturned()
    {
        //if(Constants.ENABLE_CAR_RETURN) return; // no simulation use real one

        if(!CarQueue.listOfPickups.isEmpty())
        {
            PickupProtocol pickupProtocol = CarQueue.listOfPickups.getFirst();
            CarQueue.listOfPickups.removeFirst();
            ReturnProtocol returnProtocol = new ReturnProtocol();
            returnProtocol.setId(pickupProtocol.getReservation().getCarId());
            System.out.println("Car with id " + pickupProtocol.getReservation().getCarId() + " arrived at return point");
            producerTemplate.sendBodyAndHeader("seda:queue:carToInspectQueue", returnProtocol, "carId", pickupProtocol.getReservation().getCarId());
        }
    }

}
