package carrental.model.billing;


import carrental.model.pickupPoint.ClaimType;

import java.math.BigDecimal;

/**
 * Created by Michael on 13.05.2015.
 */
public class Claim  {

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
        return costs;
    }

    public void setCosts(BigDecimal costs) {
        this.costs = costs;
    }
}
