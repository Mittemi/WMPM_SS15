package carrental.repository.reservation;

import carrental.Constants;
import carrental.model.reservation.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;

/**
 * Created by Thomas on 14.05.2015.
 */
@Repository
@PersistenceContext(unitName= Constants.RESERVATION_PU)
public interface ReserveRepository  extends CrudRepository<Reservation, Long> {
}
