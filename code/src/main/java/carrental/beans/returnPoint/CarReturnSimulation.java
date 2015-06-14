package carrental.beans.returnPoint;

import carrental.Constants;
import carrental.beans.pickupPoint.PickupBean;
import carrental.model.pickupPoint.*;
import carrental.model.reservation.Car;
import carrental.repository.reservation.CustomerRepository;
import carrental.repository.reservation.ReservationRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

        long id = CarSimulation.getNewCarId();
        PickupProtocol pickupProtocol = new PickupProtocol();
        pickupProtocol.setId(id);

        LinkedList<Claim> claimsList = new LinkedList();
        Claim claim1= new Claim();
        claim1.setClaimType(ClaimType.Mechanical);
        claim1.setDescription("Engine exploded");
        claimsList.add(claim1);
        pickupProtocol.setClaims(claimsList);

        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setCarId(CarSimulation.getNewCarId());
        reservation.setCustomerId(id);
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
    //@Scheduled(fixedRate = 3000)
    public void carReturned()
    {

    }

    @Scheduled(fixedRate = 1500)
    public void createClaimsFile() {

        if(!CarQueue.listForClaims.isEmpty()) {

            Reservation reservation = CarQueue.listForClaims.get(0);
            System.out.println("ReturnPoint: CarInspection: Claims list for car with ID=" + reservation.getCarId()+" saved to csv.");
            CarQueue.listForClaims.remove(reservation);

            List<Claim> claims = PickupBean.generateClaims();

            String filename = "claims/claims_" + reservation.getCarId() + "_" + reservation.getReservationId() + ".csv";

            File theDir = new File("claims");
            if (!theDir.exists()) {
                try{
                    theDir.mkdir();
                }
                catch(SecurityException se){
                    //handle it
                }
            }

            try(FileWriter fw = new FileWriter(filename )) {

                try(BufferedWriter bw = new BufferedWriter(fw)) {

                //    bw.write("Type;Description\n");


                    for (Claim claim : claims) {
                        bw.write(claim.getClaimType().toString());
                        bw.write(";");
                        bw.write(claim.getDescription().replace(";", ""));
                        bw.write("\n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
