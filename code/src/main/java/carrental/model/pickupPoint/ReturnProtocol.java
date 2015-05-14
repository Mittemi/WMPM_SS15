package carrental.model.pickupPoint;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Michael on 14.05.2015.
 */
@Document(collection = "returnProtocols")
public class ReturnProtocol {

    @Id
    private Long id;

    //use the reservation instead of the customer_id
    private Reservation reservation;

    private List<Claim> claims;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
