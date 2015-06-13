package carrental.model.billing;

import carrental.model.reservation.CarState;

/**
 * Created by Alexander on 28.05.2015
 */

public class Car {
	private String description;
    private String licensePlate;
    private String color;
    private int power;
    private CarState carState;
    private Double pricePerDay;
    
	public Car(String description, String licensePlate, String color,
			int power, CarState carState, Double pricePerDay) {
		super();
		this.description = description;
		this.licensePlate = licensePlate;
		this.color = color;
		this.power = power;
		this.carState = carState;
		this.pricePerDay = pricePerDay;
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
	
	public CarState getCarState() {
		return carState;
	}
	
	public void setCarState(CarState carState) {
		this.carState = carState;
	}
	
	public Double getPricePerDay() {
		return pricePerDay;
	}
	
	public void setPricePerDay(Double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}
    
    
}
