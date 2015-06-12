package carrental.model.pickupPoint;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.data.annotation.Id;

/**
 * Created by Michael on 13.05.2015.
 */
@CsvRecord(separator = ";")
public class Claim {

    public Claim() {
    }

    public Claim(ClaimType claimType, String description) {
        this.claimType = claimType;
        this.description = description;
    }

    @DataField(pos = 1)
    private ClaimType claimType;

    @DataField(pos = 2)
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
