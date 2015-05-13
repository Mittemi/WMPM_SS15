package carrental;

import carrental.repository.pickupPoint.PickupProtocolRepository;
import carrental.repository.pickupPoint.ReservationRepository;
import carrental.repository.reservation.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CarRentalApplication.class)
public class CarrentalApplicationTests {

	//NOTE: application will not terminate on unit-test!!!

	@Test
	public void contextLoads() {
	}

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	//private PickupProtocolRepository pickupProtocolRepository;
	private ReservationRepository reservationRepository;


	@Test
	public void testReservation() {
		assertNotNull(customerRepository);
		assertNull(customerRepository.findOne(1L));
	}

	@Test
	public void testPickupPoint() {
		assertNotNull(reservationRepository);
	}

}
