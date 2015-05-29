package carrental.beans.reservation;

import carrental.model.reservation.Car;
import carrental.model.reservation.CarState;
import carrental.repository.reservation.CarRepository;
import carrental.repository.reservation.CarSpecification;
import carrental.repository.reservation.CustomerRepository;
import carrental.repository.reservation.ReserveRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Thomas on 23.05.2015.
 */
@RestController
public class AvailabilityRESTService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    private ProducerTemplate producerTemplate;

    /**
     * changes the availability state of a car (e.g. after pickup a car --> CarState.INUSE; after cancel reservation
     * --> CarState.AVAILABLE; after car repaired or returned without repairing needs --> CarState.AVAILABLE
     * @param licensePlate
     * @param state
     * @return changed representation of a car, or null if the car was not found
     */
    @RequestMapping(value = "/availability", method = RequestMethod.GET, produces = "application/json")
    public CarDTO setAvailabilityState(@RequestParam(value="licensePlate", required = true)String licensePlate,
                                     @RequestParam(value="state", required = true) CarState state) {
        Car car = carRepository.findOne(CarSpecification.licensePlateIs(licensePlate));
        if (car == null) {
            return null;
        }
        car.setCarState(state);
        carRepository.save(car);
        System.out.println("Reservation: REST Service call received - Car state of " + car.getDescription() + " was " +
                "changed to: " + car.getCarState());
        return new CarDTO(car.getLicensePlate(),car.getDescription(),car.getPower(),car.getColor(),car.getCarState().toString());
    }

}
