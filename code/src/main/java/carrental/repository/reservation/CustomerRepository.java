package carrental.repository.reservation;

import carrental.model.reservation.Customer;
import carrental.Constants;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;

/**
 * Created by Michael on 13.05.2015.
 */
@Repository
@PersistenceContext(unitName= Constants.RESERVATION_PU)
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
