package carrental.beans.pickupPoint.esb;

import carrental.model.reservation.Reservation;
import carrental.repository.reservation.ReservationRepository;
import carrental.repository.reservation.ReserveRepository;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Michael on 09.06.2015.
 */
public class PullReservation {

    @Autowired
    ReserveRepository reservationRepository;

    public Reservation getReservation(Exchange exchange) {
        Long body = (Long) exchange.getIn().getBody();
        return reservationRepository.findOne(body);
    }
}
