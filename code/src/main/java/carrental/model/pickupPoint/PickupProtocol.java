package carrental.model.pickupPoint;

import carrental.model.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 13.05.2015.
 */
@Document(collection = "pickupProtocols")
public class PickupProtocol  {

    @Id
    private Long id;

    private Date pickupDate;

    //use the reservation instead of the customer_id
    private Reservation reservation;

    private List<Claim> claims;

    public PickupProtocol() {
        claims = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }
}
