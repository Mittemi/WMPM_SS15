package carrental.beans.reservation;

import carrental.model.reservation.*;
import carrental.repository.reservation.CarRepository;
import carrental.repository.reservation.CarSpecification;
import carrental.repository.reservation.CustomerRepository;
import carrental.repository.reservation.ReserveRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.*;

/**
 * Created by Thomas on 23.05.2015.
 */

@RestController
public class ReservationRESTServiceBean {

    @Autowired
    CarRepository carRepository;

    @Autowired
    ReserveRepository reserveRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public List<CarDTO> listCars(@RequestParam(value = "description",required = false) String description,
                                      @RequestParam(value = "color",required = false) String color,
                                      @RequestParam(value = "licensePlate",required = false) String licensePlate,
                                      @RequestParam(value = "minPower",required = false) Integer minPower) {

        Specifications<Car> tempo = null;

        if (description != null) {
            tempo = where(CarSpecification.descriptionIsLike(description));
        }
        if (color != null) {
            tempo = tempo == null ? where(CarSpecification.colorIsLike(color)):tempo.and(CarSpecification.colorIsLike
                    (color));
        }
        if (licensePlate != null) {
            tempo = tempo == null ? where(CarSpecification.licensePlateIs(licensePlate)):tempo.and(CarSpecification
                    .licensePlateIs(licensePlate));
        }
        if (minPower != null) {
            tempo = tempo == null ? where(CarSpecification.powerIsGreaterThanOrEqual(minPower)):tempo.and(CarSpecification
                    .powerIsGreaterThanOrEqual(minPower));
        }

        Iterable<Car> carList = carRepository.findAll(tempo);
        List<CarDTO> extCarList = new LinkedList<>();
        for (Car car : carList) {
            extCarList.add(new CarDTO(car.getLicensePlate(), car.getDescription(), car.getPower(), car.getColor(), car
                    .getCarState().toString()));
        }
        return extCarList;
    }

    @RequestMapping(value = "/chooseCar", method = RequestMethod.GET, produces = "application/json")
    public ReservationDTO chooseCar(@RequestParam(value="licensePlate", required = true) String licensePlate,
                                     @RequestParam(value="name", required = true) String name,
                                     @RequestParam(value="address", required = false) String address,
                                     @RequestParam(value="birthday", required = false) Date birthday,
                                     @RequestParam(value="sex", required = false) Sex sex,
                                     @RequestParam(value="lic", required = true) String drivingLicense) {

        Customer customer = new Customer(name, address, birthday, sex, drivingLicense);
        Car car = carRepository.findOne(CarSpecification.licensePlateIs(licensePlate));
        if (car == null) {
            return new ReservationDTO(ReservationDTO.ReservationState.FAILED,"Car with license plate: "+licensePlate
                    + " does not exist in our system.");
        }
        if (car.getCarState() != CarState.AVAILABLE) {
            return new ReservationDTO(ReservationDTO.ReservationState.FAILED, "Car " + car.getDescription() + " with" +
                    " license plate " + car.getLicensePlate()+ " is not available anymore!");
        }
        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setDate(new Date());
        reservation.setCustomer(customer);
        car.setCarState(CarState.RESERVED);
        customerRepository.save(customer);
        carRepository.save(car);
        reserveRepository.save(reservation);

        //send confirmation of successful reservation to ESB
        producerTemplate.sendBody("activemq:queue:reservationViaServiceDoneQueue", "Confirmation of successful reservation with reservation id: " + reservation.getId());

        String message = "Car " + car.getDescription() + " with license plate " + car.getLicensePlate() + " has been " +
                "successfully reserved for " + customer.getName();
        return new ReservationDTO(ReservationDTO.ReservationState.SUCCESS, message, car.getLicensePlate(), car
                .getDescription(), customer.getName(), reservation.getDate());
    }

}