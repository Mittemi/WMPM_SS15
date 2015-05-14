package carrental.beans;


import carrental.model.reservation.Reservation;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Thomas on 12.05.2015.
 */
@Component
public class ReservationBean {
    @Autowired
    private ProducerTemplate producerTemplate;


    @Scheduled(fixedDelay = 5000)
    public void onCustomerEntersStore() {
        Reservation reservation = new Reservation();

        //Process Step: Enter credentials
        enterCredentials(reservation);

        //TODO Select car
        //TODO Check availability
        //TODO Reserve car
        //TODO emit reservation to queue
    }

    private void enterCredentials(Reservation reservation) {
        //TODO thi: simulate some entered credentials
    }
}
