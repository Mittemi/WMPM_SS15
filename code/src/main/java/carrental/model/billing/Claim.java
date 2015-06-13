package carrental.model.billing;


import carrental.model.pickupPoint.ClaimType;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

/**
 * Created by Michael & Alexander on 13.05.2015.
 */
public class Claim implements Serializable {

    private ClaimType claimType;

    private String description;

    private BigDecimal costs;
    
    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getCosts() {
        return this.costs;
    }

    public void setCosts(BigDecimal costs) {
        this.costs = costs;
    }
}
