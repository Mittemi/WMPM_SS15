package carrental.model.pickupPoint;

import org.springframework.data.annotation.Id;

/**
 * Created by Michael on 13.05.2015.
 */
public class Claim {

    private ClaimType claimType;

    private String description;

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

    @Override
    public String toString() {
        return "Claim: (" + claimType + ", [" + description + "])";
    }
}
