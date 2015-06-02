package carrental.beans.reservation;

import java.util.Date;

/**
 * Created by Thomas on 23.05.2015.
 */
public class ReservationDTO {

    private ReservationState state;
    private String message;

    private String carLicensePlate;
    private String carDescription;
    private String customerName;
    private Date reservationDate;

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public void setCarLicensePlate(String carLicensePlate) {
        this.carLicensePlate = carLicensePlate;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationDTO() {

    }

    public ReservationDTO(ReservationState state, String message) {
        this.state = state;
        this.message = message;
    }

    public ReservationDTO(ReservationState state, String message, String carLicensePlate, String carDescription, String customerName, Date reservationDate) {
        this.state = state;
        this.message = message;
        this.carLicensePlate = carLicensePlate;
        this.carDescription = carDescription;
        this.customerName = customerName;
        this.reservationDate = reservationDate;
    }

    enum ReservationState {
        SUCCESS,
        FAILED
    }
}
