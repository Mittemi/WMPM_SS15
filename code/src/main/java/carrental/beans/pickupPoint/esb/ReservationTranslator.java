package carrental.beans.pickupPoint.esb;

import carrental.model.pickupPoint.Reservation;

/**
 * Created by Michael on 19.05.2015.
 */
public class ReservationTranslator {

    public Reservation process(carrental.model.reservation.Reservation reservation) {

        System.out.println("PickupPoint: MessageTranslator Reservation (reservation) --> Reservation (pickupPoint)");
        Reservation outputReservation = new Reservation();

        outputReservation.setCarId(reservation.getCar().getId());
        outputReservation.setCustomerId(reservation.getCustomer().getId());
        outputReservation.setReservationDate(reservation.getDate());
        outputReservation.setReservationId(reservation.getId());

        return outputReservation;
    }
}
