package carrental.routing.claimFixCenter;

import carrental.model.pickupPoint.Claim;

import java.io.Serializable;

/**
 * Created by Constantin on 13.06.2015.
 */
public class ClaimDTO implements Serializable {
    private String protocolType;
    private Claim claim;
    private Long carId;
    public ClaimDTO(Claim claim,long carId){
        this.setClaim(claim);
        this.setCarId(carId);
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }
}
