package carrental.beans.reservation;

import java.io.Serializable;

/**
 * Created by Thomas on 23.05.2015.
 */
public class CarDTO implements Serializable{
    String licensePlate;
    String description;
    Integer power;
    String color;
    String carState;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarState() {
        return carState;
    }

    public void setCarState(String carState) {
        this.carState = carState;
    }

    public CarDTO() {
    }

    public CarDTO(String licensePlate, String description, Integer power, String color, String carState) {
        this.licensePlate = licensePlate;
        this.description = description;
        this.power = power;
        this.color = color;
        this.carState = carState;
    }
}
