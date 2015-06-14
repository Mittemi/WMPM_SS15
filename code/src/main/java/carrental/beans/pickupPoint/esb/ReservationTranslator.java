package carrental.beans.pickupPoint.esb;

import carrental.model.pickupPoint.Reservation;

/**
 * Created by Michael on 19.05.2015.
 */
public class ReservationTranslator {

    public Reservation process(carrental.model.reservation.Reservation reservation) {

        System.out.println("ESB (PP)        : MessageTranslator Reservation (reservation) --> Reservation (pickupPoint) [ID: " + reservation.getId() + "]" );
        Reservation outputReservation = new Reservation();

        outputReservation.setCarId(reservation.getCar().getId());
        outputReservation.setLicensePlate(reservation.getCar().getLicensePlate());
        outputReservation.setCustomerId(reservation.getCustomer().getId());
        outputReservation.setReservationDate(reservation.getDate());
        outputReservation.setReservationId(reservation.getId());

        return outputReservation;
    }
}
