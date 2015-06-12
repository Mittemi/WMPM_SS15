package carrental.model.reservation;

import carrental.Constants;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Michael on 13.05.2015.
 */
@Entity
@PersistenceContext(unitName= Constants.RESERVATION_PU)
@NamedQuery(name = "findById", query = "select r from Reservation r where r.id = :id")
public class Reservation extends AbstractPersistable<Long> {

    // not as key since one customer can reserve the same car several times
    @ManyToOne(optional = false, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne(optional = false, targetEntity = Car.class)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    private Date date;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
