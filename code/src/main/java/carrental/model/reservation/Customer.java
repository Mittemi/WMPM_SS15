package carrental.model.reservation;

import carrental.Constants;
import carrental.model.Reservation;
import org.springframework.data.jpa.domain.AbstractPersistable;
import scala.util.parsing.combinator.testing.Str;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 13.05.2015.
 */
@Entity
@PersistenceContext(unitName= Constants.RESERVATION_PU)
public class Customer extends AbstractPersistable<Long> {

    private String name;
    private String address;
    private Date birthday;
    private Sex sex;
    private String drivingLicense;

    //lastdrivencar = car with highest value for reservationdate ???



    @OneToMany(mappedBy = "customer", targetEntity = carrental.model.reservation.Reservation.class)
    private List<Reservation> reservations;

    public Customer() {
        reservations = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
}
