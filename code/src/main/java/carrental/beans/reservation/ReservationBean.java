package carrental.beans.reservation;


import carrental.CarRentalConfig;
import carrental.model.reservation.*;
import carrental.repository.reservation.ReservationRepository;
import carrental.repository.reservation.CarRepository;
import carrental.repository.reservation.CustomerRepository;
import carrental.repository.reservation.ReserveRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Thomas on 12.05.2015.
 */
@Component
public class ReservationBean {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CarRentalConfig config;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    CarRepository carRepo;

    @Autowired
    ReserveRepository reservationRepo;


    private static final String[] NAMES = {"Peter Müller",  "Nathan Wilton", "Mike Pölz", "Hansi Riepl", "Constantin Fränkel", "Mara Mayer",
                                            "Sandra Fulte",  "Franziska Rothmann", "Jessica Petty" , "Melissa Komarek"};
    private static final String[] ADDRESSES = {"Schusterweg 10, 1050 Wien", "Mayerhofgasse 4, 1070 Wien", "Fredlweg 83a, 4030 Urfahr",
                                                "Ulianderstraße 1, 5349 Insmadorf", "Koreaflussstrasse 30, 7009 Windhaag"};
    private static final Date[] BIRTHDAYS = {new Date(674224680000L), new Date(624214680000L), new Date(324214680000L), new Date(414214680000L),
                                                new Date(814214680000L)};

    @PostConstruct
    public void initCarSampleData() {
        carRepo.deleteAll();
        reservationRepo.deleteAll();
        Car car1 = new Car("Ferarri 488 GTB", "FR 632 TH", "red", 492, CarState.AVAILABLE, 250d);
        Car car2 = new Car("Skoda Felicia", "FR 631 TH", "red", 55, CarState.AVAILABLE, 39d);
        Car car3 = new Car("VW Golf R", "FR 12 CH", "blue", 270, CarState.AVAILABLE, 49d);
        Car car4 = new Car("Audi A6 Quattro", "FR 75 CH", "green", 158, CarState.AVAILABLE, 69d);
        Car car5 = new Car("Kia Ceed", "W 14588 L", "black", 75, CarState.AVAILABLE, 45d);
        carRepo.save(car1);
        carRepo.save(car2);
        carRepo.save(car3);
        carRepo.save(car4);
        carRepo.save(car5);
    }

    @Scheduled(fixedDelay = 30000)
    public void onCustomerEntersStore() {
        System.out.println("Reservation: Customer entered store.");
        boolean available;
        boolean chooseAgain = false;
        Reservation reservation = new Reservation();
        Car car;

        //Process Step: Enter credentials
        enterCredentials(reservation);
        System.out.println("Reservation: Customer provided us with credentials. Name: " + reservation.getCustomer().getName()
                + ", Address: " + reservation.getCustomer().getAddress()
                + ", Birthday: " + reservation.getCustomer().getBirthday()
                + ", Driving License: " + reservation.getCustomer().getDrivingLicense());

        do {
            //Process Step: Select car
            car = selectCar();
            System.out.println("Reservation: Customer " + reservation.getCustomer().getName() + " chose car "+ car.getDescription());
            //Process Step: Check Availability
            available = checkAvailability(car);
            if (!available) {
                System.out.println("Reservation Car "+ car.getDescription() +" is currently not available");
                //chooseAgain: P(CHOOSE_AGAIN) = 0.6
                chooseAgain = Math.abs(new Random().nextInt() % 10) < 6;
                if (chooseAgain)
                    System.out.println("Reservation: Customer " + reservation.getCustomer().getName() + " tries to find another car.");
            }
        }
        while (chooseAgain);

        if (!available) {
            System.out.println("Reservation: Customer "+reservation.getCustomer().getName() + " leaves the store without choosing any car.");
            return;
        }

        reserveCar(reservation, car);
        System.out.println("Reservation: Car reservation of car: " + car.getDescription()
                + " was successfully performed for user "
                + reservation.getCustomer().getName());

        //Process Step: Send Confirmation
        sendConfirmation(reservation);
    }

    /**
     * Enters the customer credentials to the reservation and saves the customer in the DB
     * @param reservation
     */
    private void enterCredentials(Reservation reservation) {
        Random r = new Random();
        Customer c = new Customer();

        int index = Math.abs(r.nextInt()%10);
        c.setName(NAMES[index]);
        if (index < 5) {
            c.setSex(Sex.male);
        }
        else {
            c.setSex(Sex.female);
        }
        index = Math.abs(r.nextInt()%5);
        c.setAddress(ADDRESSES[index]);
        index = Math.abs(r.nextInt()%5);
        c.setBirthday(BIRTHDAYS[index]);
        c.setDrivingLicense("Lic" + Math.abs(r.nextInt() % 10000));

        reservation.setCustomer(c);
    }

    /**
     * Selects a car and returns it
     * @return car
     */
    private Car selectCar() {
        Iterable<Car> carList = carRepo.findAll();
        long count = carRepo.count();
        long index = Math.abs(new Random().nextLong()%count);
        int i = 0;
        for (Car car : carList) {
            if (index == i) {
                return car;
            }
        i++;
        }
        return null;
    }

    /**
     * checks whether the car is available or not
     * @param car
     * @return
     */
    private boolean checkAvailability(Car car) {
        return car.getCarState() == CarState.AVAILABLE;
    }

    /**
     * Reserves the car and adds it to the given reservation.
     * @param reservation
     * @param car
     */
    private void reserveCar(Reservation reservation, Car car) {
        reservation.setCar(car);
        reservation.setDate(new Date());
        car.setCarState(CarState.RESERVED);
        customerRepo.save(reservation.getCustomer());
        carRepo.save(car);
        reservationRepo.save(reservation);
    }

    /**
     * sends the confirmation of "reservation finished" to the ESB.
     * @param reservation
     */
    private void sendConfirmation(Reservation reservation) {
        producerTemplate.sendBody("activemq:queue:reservationDoneQueue", "Confirmation of successful reservation with reservation id: "+reservation.getId());
    }
}
