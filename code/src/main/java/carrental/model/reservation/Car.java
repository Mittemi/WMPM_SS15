package carrental.model.reservation;

import carrental.Constants;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 12.05.2015.
 */
@Entity
@PersistenceContext(unitName= Constants.RESERVATION_PU)
public class Car extends AbstractPersistable<Long> {

    private String description;
    private String licensePlate;
    private String color;
    private int power;
    private CarState carState;


    @OneToMany(mappedBy = "car", targetEntity = carrental.model.reservation.Reservation.class)
    private List<Reservation> reservations;

    public Car() {
        reservations = new LinkedList<>();
    }
    public List<Reservation> getReservations() {
        return reservations;
    }

    public Car(String description, String licensePlate, String color, int power, CarState carState) {
        this();
        this.description = description;
        this.licensePlate = licensePlate;
        this.color = color;
        this.power = power;
        this.carState = carState;
    }

    public CarState getCarState() {
        return carState;
    }

    public void setCarState(CarState carState) {
        this.carState = carState;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
